package sbtjaxws

import sbt.IO._
import com.sun.tools.ws.WsImport

import java.lang.System._
import sbt._
import Keys._

/**
 * Plugin to run WSIMPORT to generate java from wsdl
 * Requires WSDL URL, package name and dorectory where source files are to be generated
 */

object WSImportPlugin extends AutoPlugin {

  object autoImport {
    val SbtJaxWs = config("sbtjaxws")
    val wsimport = taskKey[Seq[File]]("Generates Java code from WSDL")
    val wsdlFile = settingKey[String]("List of wsdl files")
    val packageName = settingKey[String]("List of wsdl files")
    val sourceDir = settingKey[String]("List of wsdl files")

  }

  override def projectSettings = sbtJaxWsSettings

  override def requires = plugins.JvmPlugin

  override def trigger = allRequirements


  import autoImport._

    val sbtJaxWsSettings: Seq[Setting[_]] =
    Seq(
      javaSource in SbtJaxWs <<= sourceManaged in Compile,
      wsdlFile := "",
      packageName := "",
      sourceDir :="",
      wsimport := {
        val file = wsdlFile.value
        val marketoPackage = packageName.value
        val generated = sourceDir.value
        val s: TaskStreams = streams.value
        val log : Logger  = s.log
        runWsImport(file, generated, marketoPackage,log)
      })

  /**
   * Construct arguments to run wsimport
   * @param wsdlFile
   * @param generated
   * @param packageName
   * @return
   */
  private def makeArgs(wsdlFile: String,generated:String, packageName:String): Seq[String]  =
      Seq("-Xnocompile", "-quiet") ++
      Seq("-s", generated) ++
      Seq("-p",packageName) ++
      Seq(wsdlFile)

  /**
   * Run the java implementation of WSIMPORT utility
   * @param wsdlFile
   * @param generated
   * @param packageName
   * @param log
   * @return
   */
  private def runWsImport( wsdlFile: String,generated:String,packageName:String,log:Logger): Seq[File] = {

    java.lang.System.setProperty("javax.xml.accessExternalSchema", "all")
    val args = makeArgs(wsdlFile,generated,packageName)
    val  sourceDirectory = new File(generated)


    try {
      IO.createDirectory(sourceDirectory)
    }catch{
      case ex =>
        log.error("Error creating dir "+generated+ "  "+ex.getMessage)
    }

    try {
      log.debug("wsimport arguments " + args.mkString(" "))
      val result = WsImport.doMain(args.toArray)
      if (result == 0) {
        log.info("Generated java from WSDL successfully")

      }

    }catch {
      case t =>
        log.error("Problem running wsimport " +t.getMessage)
        log.error(t.getMessage)
    }
    sourceDirectory.listFiles()
    }


}
