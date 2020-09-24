package org.daron.distage

import scala.concurrent.Future

trait ToFuture[F[_]] {
  def toFuture[A](fa: F[A]): Future[A]
}

object ToFuture {
  def apply[F[_]](implicit tf: ToFuture[F]): ToFuture[F] = tf
}
