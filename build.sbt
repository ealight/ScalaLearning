name := "WeatherReader"

version := "0.1"

scalaVersion := "2.12.12"


val akkaVersion = "2.6.14"
val json4sVersion = "4.0.0"

enablePlugins(JavaAppPackaging)
enablePlugins(AkkaGrpcPlugin)

inConfig(Compile)(Seq(
  PB.protoSources += baseDirectory.value /  "protobufs"
))

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-projection-kafka" % "1.2.1",
  "com.typesafe.akka" %% "akka-discovery" % akkaVersion,
  "org.json4s" %% "json4s-jackson" % json4sVersion,
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

