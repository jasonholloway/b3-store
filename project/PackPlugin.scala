import com.amazonaws.services.lambda.AWSLambdaClientBuilder
import com.amazonaws.services.lambda.model.UpdateFunctionCodeRequest
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.typesafe.sbt.SbtProguard
import com.typesafe.sbt.SbtProguard.autoImport._
import sbt.PluginTrigger.AllRequirements
import sbt.Keys._
import sbtassembly.AssemblyPlugin
import sbtassembly.AssemblyPlugin.autoImport.assembly
import io.circe.syntax._
import io.circe.generic.auto._
import sbt.{AutoPlugin, TaskKey, ThisBuild}

case class PackInfo(name: String, version: String, jarPath: String)


object PackPlugin extends AutoPlugin {

  override def trigger = AllRequirements
  override def requires = AssemblyPlugin && SbtProguard

  object autoImport {
    lazy val pack = TaskKey[String]("pack", "assembles a fat jar, returns info as json")
  }

  import autoImport._

  override def projectSettings = Seq(
      inputs in Proguard := Seq(assembly.value),
      javaOptions in (Proguard, proguard) := Seq("-Xmx2G"),
      options in Proguard += ProguardOptions.keepMain("woodpigeon.bb.store.handler"),
      options in Proguard ++= Seq(
        "-dontoptimize",
        "-dontobfuscate",
        "-dontnote",
        "-dontwarn",
        "-keep,includedescriptorclasses class woodpigeon.bb.store.** { *; }"),
      pack := {
        val log = streams.value.log
        val key = s"""${name.value}-${version.value}"""
        val shrunk = (proguard in Proguard).value.head

        log.info(s"""uploading ${key} JAR to AWS S3""")

        val s3Client = AmazonS3ClientBuilder.defaultClient()
        val tm = TransferManagerBuilder.standard().withS3Client(s3Client).build()

        val upload = tm.upload("woodpigeon-images", key, shrunk)

        do {
          Thread.sleep(1000)
          log.info(s"""Transferred ${upload.getProgress.getPercentTransferred}%""")
        } while(!upload.isDone)

        val res = upload.waitForUploadResult
        log.info(s"""uploaded to S3 key ${res.getKey}""")

        log.info("updating AWS Lambda")

        val lambdaClient = AWSLambdaClientBuilder.defaultClient

        lambdaClient.updateFunctionCode(new UpdateFunctionCodeRequest()
          .withFunctionName("wibble")
          .withS3Bucket("woodpigeon-images")
          .withS3Key(key))

        log.info("done!")

        "DONE!"
    }
  )

}
