name := "rummikub"
organization := "de.htwg.se"
version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.144-R12"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.8"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % Test

libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "2.0.0-M2"

//libraryDependencies += "com.typesafe.akka" %% "akka-remote" % "2.5.8"

//libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"

//libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"