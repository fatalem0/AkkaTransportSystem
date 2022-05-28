name := "AkkaTransportSystem"

version := "0.1"

scalaVersion := "2.13.8"

lazy val akkaVersion = "2.6.19"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.12" % Test
)
