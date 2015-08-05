name := "cassandra.scalaz"

description := "Extensions for com.wda.sdbc.cassandra for use with Scalaz streaming"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "com.rocketfuel.scalaz.stream" %% "iterator" % "0.0a"
)
