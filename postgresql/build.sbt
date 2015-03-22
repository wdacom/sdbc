name := "sdbc-postgresql"

description := "An implementation of WDA SDBC for accessing PostgreSQL."

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "org.json4s" %% "json4s-jackson" % "3.2.11"
)
