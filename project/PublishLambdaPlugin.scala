import com.amazonaws.services.lambda.AWSLambdaClientBuilder
import com.amazonaws.services.lambda.model.UpdateFunctionCodeRequest
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import com.lightbend.sbt.SbtProguard
import sbt.Keys._
import sbtassembly.AssemblyPlugin
import sbtassembly.AssemblyPlugin.autoImport.assembly
import sbt.{AutoPlugin, TaskKey}


object PublishLambdaPlugin extends AutoPlugin {

  override def trigger = noTrigger
  override def requires = AssemblyPlugin && SbtProguard

  object autoImport {
    lazy val publishLambda = TaskKey[Unit]("publishLambda", "assembles a fat jar, slims it down, uploads it to S3, points lambda towards it")
  }

  import autoImport._
  import SbtProguard.autoImport._


  override def projectSettings = Seq(
      proguardInputs in Proguard := Seq(assembly.value),
      javaOptions in (Proguard, proguard) := Seq("-Xmx2G"),
      proguardOptions in Proguard += "-keep public class woodpigeon.bb.store.Handler { *; }",  // ProguardOptions.keepMain("woodpigeon.bb.store.Handler"),
      proguardOptions in Proguard ++= Seq(
        "-dontoptimize",
        "-dontobfuscate",
        "-dontnote",
        "-dontwarn"),
      publishLambda := {
        val log = streams.value.log
        val key = s"""b3/${name.value}-${version.value}.jar"""
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
          .withFunctionName("bb-commit")
          .withS3Bucket("woodpigeon-images")
          .withS3Key(key))

        log.info("done!")
    }
  )

}
