package org.daron.ctxrunner

import cats.Monad
import io.janstenpickle.trace4cats.base.context.{Lift => T4cLift, Local => T4cLocal, Provide => T4cProvide}
import io.janstenpickle.trace4cats.base.optics.{Getter, Lens}
import tofu.{WithLocal, WithRun}
import tofu.lift.{Lift => TofuLift}
import tofu.optics.{Contains, Extract}

object shims extends ShimsLowPriority {

  implicit def tofuProvide[I[_], F[_], R](implicit
    R: WithRun[F, I, R],
    I0: Monad[I],
    F0: Monad[F]
  ): T4cProvide[I, F, R] =
    new T4cProvide[I, F, R] {
      def Low: Monad[I] = I0
      def F: Monad[F]   = F0

      def lift[A](la: I[A]): F[A]             = R.lift(la)
      def ask[R1 >: R]: F[R1]                 = F.widen(R.context)
      def local[A](fa: F[A])(f: R => R): F[A] = R.local(fa)(f)
      def provide[A](fa: F[A])(r: R): I[A]    = R.runContext(fa)(r)
    }

  implicit def extractToGetter[S, A](c: Extract[S, A]): Getter[S, A] = Getter(c.extract)
  implicit def containsToLens[S, A](c: Contains[S, A]): Lens[S, A]   = Lens(c.get)(a => s => c.set(s, a))

}

trait ShimsLowPriority extends ShimsLowPriority1 {

  implicit def tofuLocal[F[_], R](implicit L: WithLocal[F, R], F0: Monad[F]): T4cLocal[F, R] =
    new T4cLocal[F, R] {
      def F: Monad[F]                         = F0
      def local[A](fa: F[A])(f: R => R): F[A] = L.local(fa)(f)
      def ask[R1 >: R]: F[R1]                 = F.widen(L.context)
    }
}

trait ShimsLowPriority1 {

  implicit def tofuLift[I[_], F[_]](implicit L: TofuLift[I, F], I0: Monad[I], F0: Monad[F]): T4cLift[I, F] =
    new T4cLift[I, F] {
      def Low: Monad[I]           = I0
      def F: Monad[F]             = F0
      def lift[A](la: I[A]): F[A] = L.lift(la)
    }
}
