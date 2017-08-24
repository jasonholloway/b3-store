package woodpigeon

import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.{Global, Phase}

class TsSchemaPlugin(val global: Global) extends Plugin {

  override val name: String = "ts-schema"
  override val description: String = "Generates TypeScript class defs from Scala case classes"

  override val components: List[PluginComponent] = List(Component)

  private object Component extends PluginComponent {
    val global: Global = TsSchemaPlugin.this.global
    import global._

    override val phaseName: String = "ts-schema"
    override val runsAfter: List[String] = List("typer")

    override def newPhase(prev: Phase) = new StdPhase(prev) {
      override def apply(unit: CompilationUnit): Unit = generateSchema(unit)
    }

    def generateSchema(unit: CompilationUnit) = {
      for(cls @ ClassDef(_, _, _, _) <- unit.body) {
        if(cls.name.toString.matches(".*Mallard.*")) {
          println("Hello!")
        }
      }

//      global.treeBrowsers.create().browse("ts-schema", List(unit))
    }
  }

}