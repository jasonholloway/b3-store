import sbt.PluginTrigger.AllRequirements
import sbt.{AutoPlugin, PluginTrigger, Plugins, TaskKey, ThisBuild, file}
import sbtassembly.AssemblyPlugin
import sbtassembly.AssemblyPlugin.autoImport.assembly
import io.circe.syntax._
import io.circe.generic.auto._


case class PackInfo(name: String, version: String, jarPath: String)


object PackPlugin extends AutoPlugin {

  override def trigger = AllRequirements
  override def requires = AssemblyPlugin

  object autoImport {
    lazy val pack = TaskKey[String]("pack", "assembles a fat jar, returns info as json")
  }

  import autoImport._

  override def projectSettings = Seq(
    pack := {
      val jar = assembly.value
      val info = PackInfo("", "", jar.getAbsolutePath)
      info.asJson.toString
    }
  )

}
