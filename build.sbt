lazy val base = project.in(file("base"))

lazy val postgresql = project.in(file("postgresql")).dependsOn(base % "test->test;compile->compile")

lazy val sqlserver = project.in(file("sqlserver")).dependsOn(base % "test->test;compile->compile")

lazy val root = project.in(file(".")).settings(publishArtifact := false).aggregate(base, postgresql, sqlserver)

organization in ThisBuild := "com.wda.sdbc"

scalaVersion in ThisBuild := "2.11.6"

crossScalaVersions := Seq("2.10.5")

version in ThisBuild := "0.4"

licenses in ThisBuild := Seq("The BSD 3-Clause License" -> url("http://opensource.org/licenses/BSD-3-Clause"))

homepage in ThisBuild := Some(url("https://github.com/wdacom/"))

(publishArtifact in Test) in ThisBuild := true

pomExtra in ThisBuild :=
  <developers>
    <developer>
      <name>Jeff Shaw</name>
      <id>shawjef3</id>
      <url>https://github.com/shawjef3/</url>
      <organization>WDA</organization>
      <organizationUrl>http://www.wda.com/</organizationUrl>
    </developer>
  </developers>
  <scm>
    <url>git@github.com:wdacom/sdbc.git</url>
    <connection>scm:git:git@github.com:wdacom/sdbc.git</connection>
  </scm>


//Some helpful compiler flags from https://tpolecat.github.io/2014/04/11/scalac-flags.html
scalacOptions in ThisBuild ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",       // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
  "-Xfuture"
)
