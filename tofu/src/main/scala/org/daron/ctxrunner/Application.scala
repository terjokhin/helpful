package org.daron.ctxrunner

import cats.data.ReaderT
import cats.effect.{ExitCode, IO, IOApp, Resource}
import tofu.logging.{Logging, Logs}

object Application extends IOApp {

  type I[A] = IO[A]
  type F[A] = ReaderT[IO, RequestContext, A]

  implicit val auth: Auth[F] = Auth.create[F]

  implicit val logger: Logging[IO] = Logs.sync[IO, IO].byName("SPAN").unsafeRunSync()

  val app = for {
    implicit0(logging: Logging[F]) <- Resource.eval(Logs.sync[I, F].byName("APP"))
    business                       <- BusinessLogic.create[I, F]
    app                            <- Routes.routes[I, F](business)
    server                         <- HttpServer.apply[I](app)
  } yield server

  val server = app.use(_ => IO.never)

  override def run(args: List[String]): IO[ExitCode] = server

}
