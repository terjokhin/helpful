package org.daron.ctxrunner

import cats.{Applicative, Monad}
import cats.effect.Resource
import tofu.logging.Logging
import tofu.syntax.logging._
import tofu.syntax.monadic._

trait BusinessLogic[F[_]] {
  def proceed(input: RequestInput): F[String]
}

object BusinessLogic {

  def apply[F[_]](implicit ev: BusinessLogic[F]): BusinessLogic[F] = ev

  def create[I[_]: Applicative, F[_]: Monad: Auth: Logging]: Resource[I, BusinessLogic[F]] = {

    val businessLogic = new BusinessLogic[F] {
      override def proceed(input: RequestInput): F[String] = for {
        user <- Auth[F].info
        _    <- info"Proceeding with: $user"
        resp  = "Hi, " + input.data
      } yield resp
    }

    Resource.pure[I, BusinessLogic[F]](businessLogic)
  }

}
