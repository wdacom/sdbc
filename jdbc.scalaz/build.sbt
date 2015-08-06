name := "jdbc.scalaz"

description := "Extensions for com.wda.sdbc.jdbc for use with Scalaz streaming"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "me.jeffshaw.scalaz.stream" %% "iterator" % "1.0a"
)
