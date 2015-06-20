name := "sbt-jaxws"

organization := "com.rocketlawyer.wsimport"

version := "0.0.13"

scalaVersion := "2.10.4"

sbtPlugin := true

libraryDependencies += "org.glassfish.ha" % "ha-api" % "3.1.8" artifacts( 
  Artifact("ha-api", "jar", "jar") 
) 


libraryDependencies += "org.jvnet.jax-ws-commons" % "jaxws-maven-plugin" % "2.3"

libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
  "org.slf4j" % "slf4j-simple" % "1.7.5")
