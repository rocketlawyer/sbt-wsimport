
name := "sbt-wsimport"
organization := "com.rocketlawyer"
sbtPlugin := true
scalaVersion := "2.10.5"

libraryDependencies ++= Seq(
  "com.sun.xml.ws" % "jaxws-tools" % "2.2.10"
)

val internalReleases = "Internal Releases" at "https://nexus.build.rocketlawyer.net/nexus/content/repositories/releases"
val internalSnapshots = "Internal Snapshots" at "https://nexus.build.rocketlawyer.net/nexus/content/repositories/snapshots"

publishTo := {
  if (isSnapshot.value) Some(internalSnapshots)
  else Some(internalReleases)
}
publishMavenStyle := true


