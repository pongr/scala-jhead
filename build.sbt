name := "scala-jhead"

organization := "com.pongr"

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "Sonatype" at "https://oss.sonatype.org/content/groups/public",
  "typesafe repo"   at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.11" % "test",
  "commons-lang" % "commons-lang" % "2.5",
  "commons-io" % "commons-io" % "1.4",
  "joda-time" % "joda-time" % "2.1",
  "org.joda" % "joda-convert" % "1.2",
  "com.typesafe.akka" % "akka-actor" % "2.0.4",
  "org.clapper" %% "grizzled-slf4j" % "0.6.10"
)

//http://www.scala-sbt.org/using_sonatype.html
//https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide
publishTo <<= version { v: String =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots/")
  else                             Some("releases" at nexus + "service/local/staging/deploy/maven2/")
}

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

licenses := Seq("Apache-2.0" -> url("http://opensource.org/licenses/Apache-2.0"))

homepage := Some(url("http://github.com/pongr/scala-jhead"))

organizationName := "Pongr"

organizationHomepage := Some(url("http://pongr.com"))

description := "JHead for scala"

pomExtra := (
  <scm>
    <url>git@github.com:pongr/scala-jhead.git</url>
    <connection>scm:git:git@github.com:pongr/scala-jhead.git</connection>
  </scm>
  <developers>
    <developer>
      <id>pcetsogtoo</id>
      <name>Byamba Tumurkhuu</name>
      <url>http://pongr.com</url>
    </developer>
    <developer>
      <id>zcox</id>
      <name>Zach Cox</name>
      <url>http://theza.ch</url>
    </developer>
  </developers>
)

seq(sbtrelease.Release.releaseSettings: _*)
