package org.daron.distage

import cats.tagless.finalAlg

import scala.concurrent.Future

@finalAlg
trait ToFuture[F[_]] {
  def toFuture[A](fa: F[A]): Future[A]
}
