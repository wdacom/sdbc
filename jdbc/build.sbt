organization := "com.rocketfuel.sdbc.jdbc"

name := "base"

libraryDependencies ++= Seq(
  //Connection pooling
  "com.zaxxer" % "HikariCP" % "2.4.0",
  "org.scodec" %% "scodec-bits" % "1.0.9",
  "com.chuusai" %% "shapeless" % "2.2.5"
)
