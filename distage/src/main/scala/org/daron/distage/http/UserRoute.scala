package org.daron.distage.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.Monad
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import org.daron.distage.ToFuture
import org.daron.distage.db.UserRepo


class UserRoute[F[_]: Monad: ToFuture](repo: UserRepo[F]) {

  def route: Route = {

    pathPrefix("users") {
      get {
        path("find" / Segment) { id =>
          onSuccess(ToFuture[F].toFuture(repo.find(id))) {
            case Some(u) => complete(u)
            case None    => complete(StatusCodes.NotFound)
          }
        }
      }
    }
  }
}
