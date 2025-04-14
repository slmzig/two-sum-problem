import Dependencies.*

import scala.collection.Seq
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.*

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    name := "two-sum-problem",
    version := "0.0.1",
    scalaVersion := "2.13.14",
    libraryDependencies ++= Seq(
          Libraries.cats,
          Libraries.catsEffectKernel,
          Libraries.catsEffectStd,
          Libraries.catsEffect,
          Libraries.circeCore,
          Libraries.circeGeneric,
          Libraries.circeGenericExtras,
          Libraries.circeParser,
          Libraries.http4sDsl,
          Libraries.http4sServer,
          Libraries.http4sClient,
          Libraries.http4sCirce,
          Libraries.pureConfig,
          Libraries.pureConfigCats,
          Libraries.pureConfigSquants,
          Libraries.log4cats,
          Libraries.scalalogging,
          Libraries.logback % Runtime,
          Libraries.scalaTest,
          Libraries.mockitoScalaTest,
          Libraries.mockitoScalaCats,
          Libraries.castsEffectTest,
          Libraries.caffeine
        ),
    dockerBaseImage := "eclipse-temurin:17-jre",
    dockerExposedPorts := Seq(3000),
    dockerAliases := {
        val base = dockerAlias.value
        Seq(base.withTag(Some(version.value)), base.withTag(Some("latest")))
      }
  )
