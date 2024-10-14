name := "MAMADODESCALA"

version := "0.1"

scalaVersion := "2.13.15"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.6.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.2.11"
)
