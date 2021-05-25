import Dependencies._

organization := "org.daron"

name := "helpful"

version := "0.0.1"

lazy val distage = project
  .in(file("distage"))
  .settings(
    common,
    name := "distage",
    scalacOptions ++= defaultOptions,
    scalacOptions += "-Ymacro-annotations",
    libraryDependencies ++= Izumi.all
  )

lazy val tofu = project
  .in(file("tofu"))
  .settings(
    common,
    name := "tofu",
    scalacOptions ++= defaultOptions,
    scalacOptions += "-Ymacro-annotations"
  )

lazy val common = Seq(
  organization := "org.daron",
  scalaVersion := "2.13.6",
  libraryDependencies ++= Akka.all ++ Cats.all ++ Circe.all ++ Tofu.all ++ MUnit.all ++ Http4s.all ++ Trace.all ++ Other.all,
  libraryDependencies += compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  libraryDependencies += compilerPlugin("org.typelevel" %% "kind-projector" % "0.13.0" cross CrossVersion.full)
)

lazy val defaultOptions = Seq(
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:reflectiveCalls",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:existentials",
  "-feature",
  "-Ywarn-unused:imports",         // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals",          // Warn if a local definition is unused.
  "-Ywarn-unused:patvars",         // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates",        // Warn if a private member is unused.
  "-Ywarn-unused:implicits",       // Warn if an import selector is not referenced.
  "-Xlint:adapted-args",           // Warn if an argument list is modified to match the receiver.
  "-Xlint:delayedinit-select",     // Selecting member of DelayedInit.
  "-Xlint:doc-detached",           // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible",           // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any",              // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator",   // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-unit",           // Warn when nullary methods return Unit.
  "-Xlint:option-implicit",        // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow",         // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align",            // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow",  // A local type parameter shadows a type already in scope.
  "-Xlint:constant"                // Evaluation of a constant arithmetic expression results in an error.
)
