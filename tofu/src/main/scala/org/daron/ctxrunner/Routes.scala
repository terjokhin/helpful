package org.daron.ctxrunner

import cats.effect.{Resource, Timer}
import cats.{Defer, Monad}
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.util.CaseInsensitiveString
import org.http4s.{HttpApp, HttpRoutes}
import tofu.WithRun
import tofu.syntax.context._
import tofu.syntax.monadic._

import java.util.concurrent.TimeUnit

object Routes {

  def routes[I[_]: Monad: Timer: Defer, F[_]: WithRun[*[_], I, RequestContext]](
    businessLogic: BusinessLogic[F]
  ): Resource[I, HttpApp[I]] = {
    val dsl = Http4sDsl[I]
    import dsl._

    val route = HttpRoutes.of[I] { case req @ GET -> Root / "hello" / name =>
      val result = for {
        now        <- Timer[I].clock.realTime(TimeUnit.MILLISECONDS)
        inetAddress = req.from.map(_.toString).getOrElse("")
        agent       = req.headers.get(CaseInsensitiveString("User-Agent")).map(_.toString()).getOrElse("")
        ctx         = RequestContext(ip = inetAddress, agent = agent, ts = now)
        input       = RequestInput(name)
        r          <- runContext[F](businessLogic.proceed(input))(ctx)
      } yield s"Response: $r"

      result.flatMap(Ok(_))
    }

    Resource.pure(Router.apply[I]("/" -> route).orNotFound)
  }
}
