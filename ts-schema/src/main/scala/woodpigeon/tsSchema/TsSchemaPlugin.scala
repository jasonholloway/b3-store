package woodpigeon.tsSchema

import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import scala.tools.nsc.{Global, Phase}
import scala.tools.nsc.io.File

class TsSchemaPlugin(val global: Global) extends Plugin {

  override val name: String = "ts-schema"
  override val description: String = "Generates TypeScript class defs from Scala case classes"

  override val components: List[PluginComponent] = List(Component)

  private var outputDir = Option.empty[String]

  private val extractOption = "([a-zA-Z-_]+):(.*)".r

  override def processOptions(options: List[String], error: (String) => Unit): Unit = {
    options.foreach {
      case extractOption(key, value) => {
        val p = key
        key match {
          case "output-dir" => outputDir = Some(value)
          case _ => ()
        }
      }
      case _ => error("No ts-schema:output-dir specified!")
    }
  }


  private object Component extends PluginComponent {
    val global: Global = TsSchemaPlugin.this.global
    import global._

    override val phaseName: String = "ts-schema"
    override val runsAfter: List[String] = List("typer")
    override val runsBefore: List[String] = List("patmat")

    override def newPhase(prev: Phase) = new StdPhase(prev) {
      override def apply(unit: CompilationUnit): Unit = generateSchema(unit)
    }

    def generateSchema(unit: CompilationUnit) : Unit = {
      findTargets(unit.body).foreach { t =>
        outputDir.foreach(dir => {
          val className = t.symbol.tpe.toString

          val path = s"$dir\\${t.symbol.tpe}.ts"

          File(path).createFile().writeAll(s"class ${className} { }")
        })
        println(t.symbol.nameString)
      }

//      global.treeBrowsers.create().browse("ts-schema", List(unit))
    }


    def allTrees(tree: Tree) : Iterator[Tree] =
      Iterator(tree) ++ tree.children.flatMap(allTrees(_))


    def findTargets(tree: Tree) = {
      allTrees(tree) flatMap {
        case n @ ClassDef(_, _, _, _)
          if n.symbol.annotations.exists(isTargetAnnotation) => Some(n)
        case _ => None
      }
    }


    val targetAnnotName = typeOf[model].toString

    def isTargetAnnotation(annot: AnnotationInfo) = annot.tpe.toString == targetAnnotName


  }

}