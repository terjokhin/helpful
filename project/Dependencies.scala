import sbt._

object Dependencies {

  object Tofu {
    private val tofuVersion = "0.7.9"
    val core                = "ru.tinkoff" %% "tofu-core"               % tofuVersion
    val derivation          = "ru.tinkoff" %% "tofu-derivation"         % tofuVersion
    val logging             = "ru.tinkoff" %% "tofu-logging"            % tofuVersion
    val loggingDerivation   = "ru.tinkoff" %% "tofu-logging-derivation" % tofuVersion
    val fs2Interop          = "ru.tinkoff" %% "tofu-fs2-interop"        % tofuVersion
    val opticsCore          = "ru.tinkoff" %% "tofu-optics-core"        % tofuVersion
    val opticsMacro         = "ru.tinkoff" %% "tofu-optics-macro"       % tofuVersion

    val all = Seq(core, derivation, logging, loggingDerivation, fs2Interop, opticsCore, opticsMacro)
  }

  object Cats {
    private val version = "2.2.0"
    val core            = "org.typelevel" %% "cats-core"   % version
    val effect          = "org.typelevel" %% "cats-effect" % version

    val all = Seq(core, effect)
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

    val all            = Seq(logbackClassic)
  }
}
