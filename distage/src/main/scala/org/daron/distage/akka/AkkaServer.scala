package org.daron.distage.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import cats.Functor
import cats.effect.{Bracket, Resource}
import cats.syntax.functor._
import distage.{ModuleDef, TagK}
import org.daron.distage.FromFuture
import org.daron.distage.http.UserRoute
import tofu.logging.Logging
import tofu.syntax.logging._

import scala.util.{Failure, Success}

object AkkaServer {

  private def bind[F[_]: FromFuture: Logging](route: Route, system: ActorSystem): F[Http.ServerBinding] = {
    implicit val ec = system.dispatcher
    implicit val s  = system

    val host = "localhost"
    val port = 8080

    FromFuture[F].fromFuture {
      val binding = Http().newServerAt(host, port).bind(route)

      binding.onComplete {
        case Success(_) => info"AkkaHttp server started on port $port"
        case Failure(_) => error"Failed to bind to $port"
      }

      binding
    }
  }

  private def unbind[F[_]: Functor: FromFuture](binding: Http.ServerBinding): F[Unit] = {
    FromFuture[F].fromFuture(binding.unbind()).void
  }

  def resource[F[_]: Functor: FromFuture: Logging](userRoute: UserRoute[F], system: ActorSystem): Resource[F, ServerBinding] =
    Resource.make(bind[F](userRoute.route, system))(unbind[F])

  def AkkaServerModule[F[_]: TagK: Logging: Bracket[*[_], Throwable]: FromFuture] = new ModuleDef {
    addImplicit[Bracket[F, Throwable]]
    make[ServerBinding].fromResource(resource[F] _)
    addImplicit[Logging[F]]
    addImplicit[FromFuture[F]]
    addImplicit[Functor[F]]
  }

}
