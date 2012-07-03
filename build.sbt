name := "scala-jhead"

organization := "com.pongr"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "Sonatype" at "https://oss.sonatype.org/content/groups/public",
  "typesafe repo"   at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.6",
  "org.specs2" %% "specs2" % "1.11" % "test",
  "commons-lang" % "commons-lang" % "2.5",
  "commons-io" % "commons-io" % "1.4",
  "joda-time" % "joda-time" % "2.1",
  "org.joda" % "joda-convert" % "1.2"
)

licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

homepage := Some(url("http://github.com/pongr/scala-jhead"))

organizationName := "Pongr"

organizationHomepage := Some(url("http://pongr.com"))

description := "JHead for scala"
