import sbt.Keys.mainClass
import sbt.addCompilerPlugin
import ReleaseTransformations._

val commonSettings = Seq(
  organization :="woodpigeon",
  scalaVersion := "2.12.2",
  scalaSource := baseDirectory.value,
  resolvers += Resolver.sonatypeRepo("releases"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
)

lazy val root = project in file(".")


lazy val store = (project in file("store"))
  .settings(commonSettings ++ Seq(
    name:="store",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.7.0",
      "io.circe" %% "circe-parser" % "0.7.0",
      "io.circe" %% "circe-generic" % "0.7.0",
      "org.scalactic" %% "scalactic" % "3.0.1",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "com.ironcorelabs" %% "cats-scalatest" % "2.2.0" % "test",
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
    ),
//    releaseProcess := Seq[ReleaseStep](
//      checkSnapshotDependencies,
//      inquireVersions,
//      runClean,
//      runTest,
//      setReleaseVersion,
//      commitReleaseVersion,
//      tagRelease,
//      publishLambda(thisProjectRef.value),
//      setNextVersion,
//      commitNextVersion,
//      pushChanges
//    )
  ))
  .enablePlugins(PublishLambdaPlugin)
  .dependsOn(macros)

lazy val `lambda-runner` = (project in file("lambda-runner"))
  .settings(commonSettings ++ Seq(
    name:="lambda-runner",
    mainClass := Some("woodpigeon.bb.store.Main"),
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
    )
  ))
  .dependsOn(store, macros)


lazy val macros = (project in file("macros"))
  .settings(commonSettings ++ Seq(
    libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.12.2"
  ))

lazy val `macros-test` = (project in file("macros-test"))
  .settings(commonSettings ++ Seq(
    addCompilerPlugin("woodpigeon" %% "ts-schema" % "0.0.1")
  ))
  .dependsOn(macros)


lazy val tsSchema = (project in file("ts-schema"))
    .settings(
      organization:="woodpigeon",
      name:="ts-schema",
      version:="0.0.1",
      scalaVersion:="2.12.3",
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-compiler" % "2.12.2",
        "org.scala-lang" % "scala-reflect" % "2.12.2",
        "org.scalactic" %% "scalactic" % "3.0.1" % "test",
        "org.scalatest" %% "scalatest" % "3.0.1" % "test"
      )
    )

lazy val packForAws = taskKey[Unit]("packages everything into one neat lambda JAR")

packForAws := {
  println("hello!")
}

