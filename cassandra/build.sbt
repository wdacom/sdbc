name := "cassandra"

description := "An implementation of WDA SDBC for accessing Apache Cassandra."

libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.6",
  "com.chuusai" %% "shapeless" % "2.2.3",
  "com.google.code.findbugs" % "jsr305" % "3.0.0"
)
