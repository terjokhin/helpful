package org.daron.distage

import cats.tagless.finalAlg

import scala.concurrent.Future

@finalAlg
trait FromFuture[F[_]] {
  def fromFuture[A](f: => Future[A]): F[A]
}
