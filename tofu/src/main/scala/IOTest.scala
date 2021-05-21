import cats.Monad
import cats.data.ReaderT
import cats.effect.{ExitCode, IO, IOApp, Sync}
import derevo.derive
import tofu.logging.derivation.loggable
import tofu.logging.derivation.loggable.generate
import tofu.logging.{LoggableContext, Logging, Logs}
import tofu.syntax.context._
import tofu.syntax.logging._
import tofu.syntax.monadic._
import tofu.{In, WithRun}

object IOTest extends IOApp {

  @derive(loggable)
  final case class RandomCtx(ts: Long, data: String)

  val ctx = RandomCtx(System.currentTimeMillis(), "I'm ctx")

  override def run(args: List[String]): IO[ExitCode] = program[ReaderT[IO, RandomCtx, *], IO].as(ExitCode.Success)

  def program[F[_]: WithRun[*[_], G, RandomCtx]: Sync, G[_]: Sync]: G[Unit] = for {
    implicit0(logger: Logging[F]) <- {
      implicit val lc: LoggableContext[F] = LoggableContext.of[F].instance[RandomCtx]
      Logs.withContext[G, F].byName("logger")
    }
    someContext                   <- Monad[G].pure(ctx)
    logUser                        = logWithCtx[F]("ttt")
    _                             <- runContext[F](logUser)(someContext)
  } yield ()

  def logWithCtx[F[_]: Logging](msg: String)(implicit c: RandomCtx In F): F[Unit] = info"$msg"

}
