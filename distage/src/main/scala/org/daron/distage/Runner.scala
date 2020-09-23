package org.daron.distage

import cats.Monad
import cats.effect.{Bracket, ExitCode, IO, IOApp, LiftIO}
import distage.{Injector, ModuleDef, Roots, TagK}
import org.daron.distage.akka.AkkaServer.AkkaServerModule
import org.daron.distage.akka.AkkaSystem.AkkaSystemModule
import org.daron.distage.db.{OrderRepo, RepoModule, UserRepo}
import org.daron.distage.domain.{Order, User}
import org.daron.distage.http.UserRoute
import tofu.logging._
import tofu.syntax.logging._

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

  def RoutesModule[F[_]: TagK: Monad: ToFuture] = new ModuleDef {
    make[UserRoute[F]]
    addImplicit[ToFuture[F]]
  }

  def Program[F[_]: TagK: Bracket[*[_], Throwable]: Logging: FromFuture: ToFuture] =
    RepoModule[F] ++ AkkaSystemModule[F] ++ AkkaServerModule[F] ++ RoutesModule[F]

  val repos = Injector().produceF[IO](Program[IO], Roots.Everything)

  override def run(args: List[String]): IO[ExitCode] = repos.use { r =>
    for {
      _  <- r.get[O].save(Order("orderId", "userdId", 100L))
      r1 <- r.get[O].find("orderId")
      _  <- info"Order is $r1"
      _  <- r.get[U].save(User("userId", "Aleksei", "Terekhin"))
      r2 <- r.get[U].find("userId")
      _  <- info"User is $r2"
      _  <- info"${r.plan.toString()}"
      _  <- IO.never
    } yield ExitCode.Success
  }
}
