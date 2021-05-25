package org.daron.ctxrunner

import cats.Monad
import tofu.WithContext
import tofu.syntax.monadic._

trait Auth[F[_]] {
  def info: F[User]
}

object Auth {

  def apply[F[_]](implicit ev: Auth[F]): Auth[F] = ev

  def create[F[_]: Monad](implicit c: F WithContext RequestContext): Auth[F] = new Auth[F] {
    override def info: F[User] = c.context.map(ctx => User(ctx.ip))
  }
}
