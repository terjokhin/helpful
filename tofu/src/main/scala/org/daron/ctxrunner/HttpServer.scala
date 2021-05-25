package org.daron.ctxrunner

import cats.effect.{ConcurrentEffect, Resource, Timer}
import org.http4s._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object HttpServer {

  def apply[F[_]: ConcurrentEffect: Timer](
    app: HttpApp[F],
    port: Int = 8080,
    host: String = "localhost",
    ec: ExecutionContext = scala.concurrent.ExecutionContext.global
  ): Resource[F, Server[F]] = BlazeServerBuilder[F](ec).bindHttp(port, host).withHttpApp(app).resource

}
