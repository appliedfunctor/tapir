
val Http4sVersion          = "0.21.18"
val CirceVersion           = "0.14.1"
val LogbackVersion         = "1.2.5"
val TapirVersion           = "0.17.20"

lazy val root = (project in file("."))
  .enablePlugins(UniversalPlugin, JavaServerAppPackaging)
  .settings(
    organization := "com.ajsworton",
    name := "tapir",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.4",
    libraryDependencies ++= Seq(
      "org.http4s"                  %% "http4s-ember-server"      % Http4sVersion,
      "org.http4s"                  %% "http4s-ember-client"      % Http4sVersion,
      "org.http4s"                  %% "http4s-circe"             % Http4sVersion,
      "org.http4s"                  %% "http4s-dsl"               % Http4sVersion,
      "io.circe"                    %% "circe-generic"            % CirceVersion,
      "ch.qos.logback"              % "logback-classic"           % LogbackVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-core"               % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s"  % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-redoc-http4s"       % TapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-cats"               % TapirVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )

scalacOptions ++= Seq(
  "-deprecation:false",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-Wunused:imports",
  "-feature",
  "-Ywarn-macros:after"
)
