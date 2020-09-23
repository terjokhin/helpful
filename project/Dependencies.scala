import sbt._

object Dependencies {

  object Akka {
    private val version = "2.6.8"
    val actor           = "com.typesafe.akka" %% "akka-actor"   % version
    val stream          = "com.typesafe.akka" %% "akka-stream"  % version
    val testkit         = "com.typesafe.akka" %% "akka-testkit" % version

    private val versionHttp = "10.2.0"
    val http                = "com.typesafe.akka" %% "akka-http-core"    % versionHttp
    val httpTestkit         = "com.typesafe.akka" %% "akka-http-testkit" % versionHttp
    val httpCirce           = "de.heikoseeberger" %% "akka-http-circe"   % "1.34.0"

    val all = Seq(actor, stream, testkit, http, httpTestkit, httpCirce)
  }

  object Tofu {
    private val tofuVersion = "0.7.9"
    val core                = "ru.tinkoff" %% "tofu-core"       % tofuVersion
    val derivation          = "ru.tinkoff" %% "tofu-derivation" % tofuVersion
    val logging             = "ru.tinkoff" %% "tofu-logging"    % tofuVersion

    val all = Seq(core, derivation, logging)
  }

  object Cats {
    private val version = "2.2.0"
    val core            = "org.typelevel" %% "cats-core"   % version
    val effect          = "org.typelevel" %% "cats-effect" % version

    private val taglessVersion = "0.11"
    val tagless                = "org.typelevel" %% "cats-tagless-macros" % taglessVersion

    val all = Seq(core, effect, tagless)
  }

  object Circe {
    private val version = "0.13.0"
    val core            = "io.circe" %% "circe-core"    % version
    val generic         = "io.circe" %% "circe-generic" % version

    val all = Seq(core, generic)
  }

  object MUnit {
    private val version = "0.7.12"
    val munit           = "org.scalameta" %% "munit" % version % Test

    val all = Seq(munit)
  }

  object Izumi {
    private val version = "0.10.19"
    val core            = "io.7mind.izumi" %% "distage-core" % version

    val all = Seq(core)
  }

  object Other {
    val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"

    val all = Seq(logbackClassic)
  }
}
