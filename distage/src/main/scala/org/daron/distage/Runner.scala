package org.daron.distage

import cats.effect.{ExitCode, IO, IOApp}
import tofu.logging._
import tofu.syntax.logging._

object Runner extends IOApp {

  val logs = Logs.sync[IO, IO]

  override def run(args: List[String]): IO[ExitCode] = for {
    implicit0(logger: Logging[IO]) <- logs.byName("App")
    _                              <- info"Hello!"
  } yield ExitCode.Success
}
