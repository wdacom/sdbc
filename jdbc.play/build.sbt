name := "jdbc.play"

description := "Extensions for com.wda.sdbc.jdbc for use with Play Iteratees"

resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-iteratees" % "2.3.8"
)

publishArtifact in Test := true
