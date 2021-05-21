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
    private val tofuVersion = "0.10.2"
    val core                = "tf.tofu" %% "tofu-core"       % tofuVersion
    val concurrent          = "tf.tofu" %% "tofu-concurrent" % tofuVersion
    val derivation          = "tf.tofu" %% "tofu-derivation" % tofuVersion
    val logging             = "tf.tofu" %% "tofu-logging"    % tofuVersion

    val all = Seq(core, concurrent, derivation, logging)
  }

  object Cats {
    private val version = "2.6.1"
    val core            = "org.typelevel" %% "cats-core"   % version
    val effect          = "org.typelevel" %% "cats-effect" % "2.5.1"

    private val taglessVersion = "0.14.0"
    val tagless                = "org.typelevel" %% "cats-tagless-macros" % taglessVersion

    val all = Seq(core, effect, tagless)
  }

  object Circe {
    private val version = "0.13.0"
    val core            = "io.circe" %% "circe-core"           % version
    val generic         = "io.circe" %% "circe-generic"        % version
    val extras          = "io.circe" %% "circe-generic-extras" % version
    val parser          = "io.circe" %% "circe-parser"         % version

    val all             = Seq(core, generic, extras, parser)
  }

  object Http4s {
    private val version = "0.21.23"

    val dsl             = "org.http4s" %% "http4s-dsl"          % version
    val server          = "org.http4s" %% "http4s-blaze-server" % version
    val json            = "org.http4s" %% "http4s-circe"        % version
    val all             = Seq(dsl, server, json)
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
