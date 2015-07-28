# Usage
Add the following to your `project/metabuild.sbt`:
```
addSbtPlugin("com.rocketlawyer" % "sbt-wsimport" % "0.0.1-SNAPSHOT")
```
Note that you'll want to replace the version string with the version of your choice.

You'll want to enable the plugin for your project in `build.sbt`:
```
val root = project.in(file(".")).enablePlugins(WsImportPlugin)
```

You then drop your WSDL into `src/main/wsdl` and Java sources will be generated
as part of compilation. You will probably want to override the `wsdlPackageName`
setting in your project's `build.sbt` to control the package for the generated
sources.
