scalaVersion := "2.12.4"

val circeVersion = "0.8.0"
val awsSdkVersion = "1.11.179"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "com.amazonaws" % "aws-java-sdk-core" % awsSdkVersion,
  "com.amazonaws" % "aws-java-sdk-s3" % awsSdkVersion,
  "com.amazonaws" % "aws-java-sdk-lambda" % awsSdkVersion,
//  "com.trueaccord.scalapb" %% "compilerplugin-shaded" % "0.6.4-SHADED"
)

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.6")
addSbtPlugin("com.lightbend.sbt" % "sbt-proguard" % "0.3.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.20")

