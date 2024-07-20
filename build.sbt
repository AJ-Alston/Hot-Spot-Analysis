import sbt.Keys.{libraryDependencies, scalaVersion, version}

lazy val root = (project in file("."))
  .settings(
    name := "CSE512-Hotspot-Analysis-Template",
    version := "0.1.0",
    scalaVersion := "2.12.18",
    crossScalaVersions := Seq("2.12.18", "2.13.14"), // Added crossScalaVersions here
    organization := "org.datasyslab",
    publishMavenStyle := true,
    mainClass := Some("cse512.Entrance")
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.1" % Provided,
  "org.apache.spark" %% "spark-sql" % "3.5.1" % Provided,
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "org.specs2" %% "specs2-core" % "4.10.6" % Test,
  "org.specs2" %% "specs2-junit" % "4.10.6" % Test
)