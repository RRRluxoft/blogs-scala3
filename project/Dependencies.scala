import sbt._

object Versions {
  val AkkaVersion = "2.6.19"
  val Slf4jVersion = "2.0.0-alpha7"
}

object Dependencies {
  import Versions._

  private val akka = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion
  )

  private val logs = Seq(
    "org.slf4j" % "slf4j-api" % Slf4jVersion
  )

  val applications = akka ++ logs
}
