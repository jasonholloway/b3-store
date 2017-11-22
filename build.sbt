import sbt.Keys.mainClass

val commonSettings = Seq(
  organization := "com.woodpigeon",
  scalaVersion := "2.12.2"
)

lazy val packageNpm = taskKey[File]("pack into NPM package")


lazy val b3 = project in file(".")


val jsSettings = Seq(
  target := target.value / "js",
  scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  skip in packageJSDependencies := false,
  scalacOptions += "-P:scalajs:sjsDefinedByDefault",
  packageNpm := {
    val npmDir = target.value / "npm"
    val resourceDir = (resources in Compile).value.head

    val fileMap = Seq(
      ((fastOptJS in Compile).value.data, "index.js"),
      ((packageJSDependencies in Compile).value, "deps.js"),
      (resourceDir / "package.json", "package.json"),
      (resourceDir / "index.d.ts", "index.d.ts")
    )

    for((f, name) <- fileMap) {
      IO.copyFile(f, npmDir / name)
    }

    npmDir
  }
)

val jvmSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided",
    "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  )
)



lazy val schema = (crossProject in file("schema"))
  .settings(commonSettings ++ Seq(
    name := "b3.schema",
    libraryDependencies ++= Seq(
      "com.trueaccord.scalapb" %%% "scalapb-runtime" % "0.6.5",
      //   "com.trueaccord.scalapb" %%% "scalapb-runtime" % "0.6.5" % "protobuf"
    )
  ))

lazy val schemaJVM = schema.jvm
    .settings(jvmSettings)

lazy val schemaJS = schema.js
    .settings(jsSettings)
    .enablePlugins(ScalaJSPlugin)



lazy val data = (crossProject in file("data"))
    .settings(
      name := "b3.data",
      libraryDependencies ++= dataDependencies
    )

val dataDependencies = Seq(
  "org.scala-lang.modules" %% "scala-async" % "0.9.6"
)

lazy val dataJVM = data.jvm
    .settings(jvmSettings)
    .dependsOn(schema.jvm)

lazy val dataJS = data.js
    .settings(jsSettings,
      libraryDependencies ++= Seq(
        "org.julienrf" %%% "faithful" % "1.0.0"
      ),
      resolvers += Resolver.sonatypeRepo("releases")
    )
    .dependsOn(schema.js)
    .enablePlugins(ScalaJSPlugin)


lazy val dataShared = (project in file("data/shared"))
  .settings(jvmSettings,
    libraryDependencies ++= dataDependencies
  )
  .dependsOn(schema.jvm)





lazy val store = (project in file("store"))
  .settings(commonSettings ++ Seq(
    name:="b3.store",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
      "commons-io" % "commons-io" % "2.5",
      "org.scalactic" %% "scalactic" % "3.0.1",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "com.ironcorelabs" %% "cats-scalatest" % "2.2.0" % "test",
    )))
  .dependsOn(schemaJVM)


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
//  .enablePlugins(PublishLambdaPlugin)

lazy val `lambda-runner` = (project in file("lambda-runner"))
  .settings(commonSettings ++ Seq(
    name:="lambda-runner",
    mainClass := Some("woodpigeon.bb.store.Main"),
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
    )
  ))
  .dependsOn(dataJVM, store)


lazy val packForAws = taskKey[Unit]("packages everything into one neat lambda JAR")

packForAws := {
  println("hello!")
}
