organization := "com.wda.sdbc.cassandra"

name := "datastax"

description := "An implementation of WDA SDBC for accessing Apache Cassandra."

libraryDependencies ++= Seq(
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.7.1",
  "com.chuusai" %% "shapeless" % "2.2.5",
  "com.google.code.findbugs" % "jsr305" % "3.0.0",
  "org.cassandraunit" % "cassandra-unit" % "2.1.3.1" % "test"
)
