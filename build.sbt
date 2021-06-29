name := "atm-ws"
scalaVersion := "2.12.12"

val akkaVersion = "2.5.26"
val akkaHttpVersion = "10.1.11"
val testVersion = "3.2.9"

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-json4s" % "0.11.0",
  "org.scalactic" %% "scalactic" % testVersion,
  "org.scalatest" %% "scalatest" % testVersion % "test",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
  "com.lightbend.akka" %% "akka-projection-kafka" % "1.2.1",
)

