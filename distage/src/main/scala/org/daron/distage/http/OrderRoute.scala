package org.daron.distage.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import distage.{ModuleDef, TagK}
import org.daron.distage.ToFuture
import org.daron.distage.db.OrderRepo
import org.daron.distage.domain.Order

import scala.util.{Failure, Success}

class OrderRoute[F[_]: ToFuture](repo: OrderRepo[F]) {

  def route: Route = pathPrefix("orders") {
    {
      (get & path(Segment)) { id =>
        onSuccess(ToFuture[F].toFuture(repo.find(id))) {
          case Some(u) => complete(u)
          case None    => complete(StatusCodes.NotFound)
        }
      }
    } ~ (post & entity(as[Order])) { o =>
      onComplete(ToFuture[F].toFuture(repo.save(o))) {
        case Success(saved) => complete(saved)
        case Failure(_)     => complete(StatusCodes.BadRequest)
      }

    }
  }
}

object OrderRoute {

  def OrderRouteModule[F[_]: TagK: ToFuture] = new ModuleDef {
    many[Route].add { new OrderRoute[F](_: OrderRepo[F]).route }
    addImplicit[ToFuture[F]]
  }
}
