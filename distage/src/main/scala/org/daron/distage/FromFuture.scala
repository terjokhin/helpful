package org.daron.distage

import scala.concurrent.Future

trait FromFuture[F[_]] {
  def fromFuture[A](f: => Future[A]): F[A]
}

object FromFuture {
  def apply[F[_]](implicit ff: FromFuture[F]): FromFuture[F] = ff
}
