package org.daron.distage.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.Monad
import cats.effect.{Bracket, Resource}
import distage.{ModuleDef, TagK}
import org.daron.distage.FromFuture
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

import scala.util.{Failure, Success}

object AkkaServer {

  private def bind[F[_]: Monad: FromFuture: Logging](routes: Set[Route], system: ActorSystem): F[Http.ServerBinding] = {
    implicit val ec = system.dispatcher
    implicit val s  = system

    val host = "localhost"
    val port = 8080

    val route = routes.reduce(_ ~ _)

    val binding: F[ServerBinding] = FromFuture[F].fromFuture {
      val binding = Http().newServerAt(host, port).bind(route)

      binding.onComplete {
        case Success(_) => println(s"AkkaHttp server started on port $port")
        case Failure(_) => println(s"Failed to bind to $port")
      }

      binding
    }

    for {
      _ <- info"Starting server"
      b <- binding
      _ <- info"Started"
    } yield b
  }

  private def unbind[F[_]: Monad: FromFuture: Logging](binding: Http.ServerBinding): F[Unit] = for {
    _ <- info"Stopping server"
    _ <- FromFuture[F].fromFuture(binding.unbind())
    _ <- info"Stopped"
  } yield ()

  def resource[F[_]: Monad: FromFuture: Logging](routes: Set[Route], system: ActorSystem): Resource[F, ServerBinding] =
    Resource.make(bind[F](routes, system))(unbind[F])

  def AkkaServerModule[F[_]: TagK: Logging: Bracket[*[_], Throwable]: FromFuture] = new ModuleDef {
    addImplicit[Bracket[F, Throwable]]
    make[ServerBinding].fromResource(resource[F] _)
    addImplicit[Logging[F]]
    addImplicit[FromFuture[F]]
    addImplicit[Monad[F]]
  }

}
