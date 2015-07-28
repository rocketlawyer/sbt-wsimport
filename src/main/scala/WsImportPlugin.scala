package com.rocketlawyer
package wsimport

import sbt.IO._
import com.sun.tools.ws.WsImport

import sbt._
import Keys._

/**
 * Plugin to run WSIMPORT to generate java from wsdl
 * Requires WSDL URL, package name and dorectory where source files are to be generated
 */

object WsImportPlugin extends AutoPlugin {

  object autoImport {
    val wsdlImport = taskKey[Seq[File]]("Generate Java sources from WSDL")
    val wsdlSource = settingKey[File]("Default WSDL source directory.")
    val wsdlPackageName = settingKey[File => String]("Package to for Java sources generated from WSDL")
    val wsdlOutputDirectory = settingKey[File]("Where to put Java sources generated from WSDL")
    val wsdlFiles = taskKey[Seq[File]]("WSDL files to process")
  }

  import autoImport._

  override def requires = plugins.JvmPlugin

  override def trigger = noTrigger

  override def projectSettings = Seq(
    wsdlSource := (sourceDirectory in Compile).value / "wsdl",
    wsdlOutputDirectory := sourceManaged.value / "wsdlImport",
    managedSourceDirectories in Compile += wsdlOutputDirectory.value,
    wsdlImport := generate.value,
    sourceGenerators in Compile += wsdlImport.taskValue,
    wsdlPackageName := (_ => organization.value),
    wsdlFiles := (wsdlSource.value ** "*.wsdl").get
  )

  def generate: Def.Initialize[Task[Seq[File]]] = Def.task {
    val files = wsdlFiles.value
    val pkg = wsdlPackageName.value
    val log = streams.value.log
    val outputDirectory = wsdlOutputDirectory.value
    IO.delete(outputDirectory)
    IO.createDirectory(outputDirectory)
    files.foreach { wsdlFile =>
      val packageName = pkg(wsdlFile)
      val args = Array(
        "-XadditionalHeaders", 
        "-Xnocompile", "-quiet",
        "-s", outputDirectory.getCanonicalPath,
        "-p", pkg(wsdlFile),
        wsdlFile.getCanonicalPath
      )
      sys.props("javax.xml.accessExternalSchema") = "all"
      val resultCode = WsImport.doMain(args)
      if (resultCode == 0) log.info(s"Successfully generated Java sources from $wsdlFile")
      else log.error(s"Failed to generate Java sources from $wsdlFile")
    }
    (outputDirectory ** "*.java").get
  }

}
