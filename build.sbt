name := """hitman"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  cache,
  ws,
  specs2 % Test,
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
// libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
// libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
// libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.2"
libraryDependencies ++= Seq("org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test")

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
