import Dependencies.*

import scala.collection.Seq

name := "two-sum-problem"

version := "0.1.0"

//scalaVersion := "2.13.10"

//lazy val root = (project in file("."))
//  .enablePlugins(JavaAppPackaging)
//  .settings(
//    organization := "com.megogo.assignment",
//    name := "test-assignment",
//    version := "0.0.1",
//    scalaVersion := "2.13.10",
//    maxErrors := 30,
//    libraryDependencies ++= Seq(
//      Libraries.cats,
//      Libraries.catsEffectKernel,
//      Libraries.catsEffectStd,
//      Libraries.catsEffect,
//      Libraries.circeCore,
//      Libraries.circeGeneric,
//      Libraries.circeGenericExtras,
//      Libraries.circeParser,
//      Libraries.http4sDsl,
//      Libraries.http4sServer,
//      Libraries.http4sClient,
//      Libraries.http4sCirce,
//      Libraries.pureConfig,
//      Libraries.pureConfigCats,
//      Libraries.pureConfigSquants,
//      Libraries.log4cats,
//      Libraries.scalalogging,
//      Libraries.logback % Runtime,
//      Libraries.scalaTest,
//      Libraries.mockitoScalaTest,
//      Libraries.mockitoScalaCats,
//      Libraries.castsEffectTest,
//    ),
//    Test    / scalacOptions -= "-Ywarn-dead-code",
//  )

scalaVersion := "2.13.14"

val Http4sVersion     = "0.23.16"
val CirceVersion      = "0.14.10"
val CatsEffectVersion = "3.5.4"

val log4catsVersion     = "2.6.0"
val scalaLoggingVersion = "3.9.5"
val logbackVersion      = "1.4.14"

libraryDependencies ++= Seq(
  "org.http4s"                 %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"                 %% "http4s-circe"        % Http4sVersion,
  "org.http4s"                 %% "http4s-dsl"          % Http4sVersion,
  "io.circe"                   %% "circe-generic"       % CirceVersion,
  "org.typelevel"              %% "cats-effect"         % CatsEffectVersion,
  "org.http4s"                 %% "http4s-blaze-client" % Http4sVersion,
  "com.github.pureconfig"      %% "pureconfig"          % "0.17.7",
  "org.typelevel"              %% "log4cats-slf4j"      % log4catsVersion,
  "com.typesafe.scala-logging" %% "scala-logging"       % scalaLoggingVersion,
  "ch.qos.logback"             % "logback-classic"      % logbackVersion
)
