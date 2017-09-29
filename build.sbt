import sbt.Keys.mainClass

val commonSettings = Seq(
  organization := "com.woodpigeon",
  scalaVersion := "2.12.2"
)

val jsSettings = Seq(
  target := target.value / "js",
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  fastOptJS in Compile := {
    IO.copyFile(file("data/src/main/resources/package.json"), file("data/target/js/scala-2.12/package.json"))
    IO.copyFile(file("data/src/main/resources/index.d.ts"), file("data/target/js/scala-2.12/index.d.ts"))
    (fastOptJS in Compile).value
  }
)

val jvmSettings = Seq(
  libraryDependencies += "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided"
)


lazy val root = (project in file("."))
//    .aggregate(`data-jvm`, `data-js`, `lambda-runner`)



val schemaSettings = commonSettings ++ Seq(
  name := "b3.schema",
  libraryDependencies ++= Seq(
    "com.trueaccord.scalapb" %%% "scalapb-runtime" % "0.6.5",
 //   "com.trueaccord.scalapb" %%% "scalapb-runtime" % "0.6.5" % "protobuf"
  )
)

lazy val schema = (project in file("schema"))
  .settings(schemaSettings, jvmSettings)

lazy val `schema-js` = (project in file("schema"))
  .settings(schemaSettings, jsSettings)
  .enablePlugins(ScalaJSPlugin)




val dataSettings = commonSettings ++ Seq(
  name := "b3.data"
)

lazy val `data-js` = (project in file("data"))
    .settings(dataSettings, jsSettings)
    .enablePlugins(ScalaJSPlugin)
    .dependsOn(`schema-js`)

lazy val data = (project in file("data"))
    .settings(dataSettings, jvmSettings)
    .dependsOn(schema)


lazy val store = (project in file("store"))
  .settings(commonSettings ++ Seq(
    name:="b3.store",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
      "commons-io" % "commons-io" % "2.5",
      "org.scalactic" %% "scalactic" % "3.0.1",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "com.ironcorelabs" %% "cats-scalatest" % "2.2.0" % "test",
    )
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
  .dependsOn(schema)
  .enablePlugins(PublishLambdaPlugin)

lazy val `lambda-runner` = (project in file("lambda-runner"))
  .settings(commonSettings ++ Seq(
    name:="lambda-runner",
    mainClass := Some("woodpigeon.bb.store.Main"),
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
    )
  ))
  .dependsOn(store)


lazy val packForAws = taskKey[Unit]("packages everything into one neat lambda JAR")

packForAws := {
  println("hello!")
}

