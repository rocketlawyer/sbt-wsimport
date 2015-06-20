This plugin generates java source from any wsdl into any directory mentioned in build.sbt.


Configuration
-------------

Put this in your `project/plugins.sbt`:

    libraryDependencies += "org.glassfish.ha" % "ha-api" % "3.1.8" artifacts(
      Artifact("ha-api", "jar", "jar")
    )
addSbtPlugin("com.rocketlawyer.wsimport" % "sbt-jaxws" % "0.0.13")



configuration example (in `build.sbt`):

wsdlFile := "http://148-CGS-511.mktoapi.com/soap/mktows/2_9?WSDL"

packageName := "com.marketo.www.mktows"

sourceDir := "/<Home>/rl-email/service/src/main/java"


Usage 
------

 sbt wsimport
