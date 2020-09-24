package org.daron.distage

import cats.effect.{Bracket, ExitCode, IO, IOApp}
import distage.{Injector, Roots, TagK}
import izumi.distage.model.Locator
import org.daron.distage.akka.AkkaServer.AkkaServerModule
import org.daron.distage.akka.AkkaSystem.AkkaSystemModule
import org.daron.distage.db.{OrderRepo, RepoModule, UserRepo}
import org.daron.distage.domain.{Order, User}
import org.daron.distage.http.{OrderRoute, UserRoute}
import tofu.logging._

import scala.concurrent.Future

object Runner extends IOApp {

  type O = OrderRepo[IO]
  type U = UserRepo[IO]

  val logs                          = Logs.sync[IO, IO]
  implicit val logging: Logging[IO] = logs.byName("App").unsafeRunSync()

  implicit val ff: FromFuture[IO] = new FromFuture[IO] {
    override def fromFuture[A](f: => Future[A]): IO[A] = IO.fromFuture(IO(f))
  }

  implicit val tf: ToFuture[IO] = new ToFuture[IO] {
    override def toFuture[A](fa: IO[A]): Future[A] = fa.unsafeToFuture()
  }

  val application = prepareModules

  override def run(args: List[String]): IO[ExitCode] = application.use { locator =>
    fillSomeData(locator) *> IO.never as ExitCode.Success
  }

  private def fillSomeData(l: Locator): IO[Unit] = for {
    _ <- l.get[O].save(Order("orderId", "userdId", 100L))
    _ <- l.get[U].save(User("userId", "Aleksei", "Terekhin"))
  } yield ()

  private def prepareModules = {
    def RoutesModule[F[_]: TagK: ToFuture] = UserRoute.UserRouteModule[F] ++ OrderRoute.OrderRouteModule[F]

    def Program[F[_]: TagK: Bracket[*[_], Throwable]: Logging: FromFuture: ToFuture] =
      RepoModule[F] ++ AkkaSystemModule[F] ++ AkkaServerModule[F] ++ RoutesModule[F]

    Injector().produceF[IO](Program[IO], Roots.Everything)
  }
}
