name := "jdbc.scalaz"

description := "Extensions for com.wda.sdbc for use with Scalaz"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.scalaz.stream" %% "scalaz-stream" % "0.7a"
)

publishArtifact in Test := true
