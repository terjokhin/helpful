package org.daron.distage.akka

import akka.actor.ActorSystem
import cats.Monad
import cats.effect.{Bracket, Resource}
import distage.{ModuleDef, TagK}
import org.daron.distage.FromFuture
import tofu.lift.UnsafeExecFuture
import tofu.syntax.monadic._

object AkkaSystem {

  private def acquire[F[_]: Monad]: F[ActorSystem] = {
    Monad[F].pure(ActorSystem())
  }

  private def release[F[_]: Monad: FromFuture] = { as: ActorSystem =>
    FromFuture[F].fromFuture(as.terminate()).void
  }

  def resource[F[_]: Monad: FromFuture]: Resource[F, ActorSystem] = Resource.make(acquire)(release)

  def AkkaSystemModule[F[_]: TagK: Bracket[*[_], Throwable]: FromFuture] = new ModuleDef {
    make[ActorSystem].fromResource(resource)
    addImplicit[Bracket[F, Throwable]]
    addImplicit[FromFuture[F]]
  }
}
