name := "atm-ws"
scalaVersion := "2.12.12"

val akkaVersion = "2.5.26"
val akkaHttpVersion = "10.1.11"
val testVersion = "3.2.9"
val json4sVersion = "4.0.0"

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % testVersion,
  "org.scalatest" %% "scalatest" % testVersion % "test",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "org.json4s" %% "json4s-native" % json4sVersion,
  "org.json4s" %% "json4s-jackson" % json4sVersion
)