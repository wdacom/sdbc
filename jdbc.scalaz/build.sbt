name := "jdbc.scalaz"

description := "Extensions for com.wda.sdbc.jdbc for use with Scalaz streaming"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.scalaz.stream" %% "scalaz-stream" % "0.7a"
)
