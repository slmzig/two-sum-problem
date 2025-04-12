import sbt.*

object Dependencies {

  object V {
    val cats         = "2.9.0"
    val catsEffect   = "3.5.0"
    val circe        = "0.14.3"
    val http4s       = "0.23.15"
    val log4cats     = "2.6.0"
    val pureConfig   = "0.17.4"
    val logback      = "1.4.8"
    val scalalogging = "3.9.5"

    val scalaTest           = "3.2.16"
    val mockitoScalaTest    = "1.17.14"
    val catsEffectScalaTest = "1.5.0"

  }

  object Libraries {
    def circe(artifact: String): ModuleID  = "io.circe"     %% s"circe-$artifact"  % V.circe
    def http4s(artifact: String): ModuleID = "org.http4s"   %% s"http4s-$artifact" % V.http4s

    val cats             = "org.typelevel" %% "cats-core"          % V.cats
    val catsEffectKernel = "org.typelevel" %% "cats-effect-kernel" % V.catsEffect
    val catsEffectStd    = "org.typelevel" %% "cats-effect-std"    % V.catsEffect
    val catsEffect       = "org.typelevel" %% "cats-effect"        % V.catsEffect

    val circeCore          = circe("core")
    val circeGeneric       = circe("generic")
    val circeGenericExtras = circe("generic-extras")
    val circeParser        = circe("parser")

    val http4sDsl    = http4s("dsl")
    val http4sServer = http4s("blaze-server")
    val http4sClient = http4s("blaze-client")
    val http4sCirce  = http4s("circe")

    val pureConfig        = "com.github.pureconfig" %% "pureconfig"             % V.pureConfig
    val pureConfigCats    = "com.github.pureconfig" %% "pureconfig-cats-effect" % V.pureConfig
    val pureConfigSquants = "com.github.pureconfig" %% "pureconfig-squants"     % V.pureConfig

    val log4cats     = "org.typelevel"              %% "log4cats-slf4j" % V.log4cats
    val scalalogging = "com.typesafe.scala-logging" %% "scala-logging"  % V.scalalogging
    val logback      = "ch.qos.logback"             % "logback-classic" % V.logback

    val scalaTest          = "org.scalatest"          %% "scalatest"                              % V.scalaTest           % Test
    val mockitoScalaTest   = "org.mockito"            %% "mockito-scala-scalatest"                % V.mockitoScalaTest    % Test
    val mockitoScalaCats   = "org.mockito"            %% "mockito-scala-cats"                     % V.mockitoScalaTest    % Test
    val castsEffectTest    = "org.typelevel"          %% "cats-effect-testing-scalatest"          % V.catsEffectScalaTest % Test

  }
}
