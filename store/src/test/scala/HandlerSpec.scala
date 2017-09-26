package com.woodpigeon.b3.store

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.Context
import com.trueaccord.lenses.Lens
import com.woodpigeon.b3.store.v100.Command
import com.woodpigeon.b3.store.v100._
import org.scalatest.{FlatSpec, Matchers}

class HandlerSpec extends FlatSpec with Matchers {

  "Handler" should "happily receive protobuf messages" in {
    val command = Command()
                  .withId("123")
                  .withPutProduct(PutProduct("pants-1", "Longjohns"))

    val r : Lens[Command, Command] = null

    val p = command.update(_.id := "5")

    val outp = handle(Command.toByteArray(command))
    val result = Result.parseFrom(outp)

    result.ok shouldBe true
    result.id shouldBe command.id
  }

  def handle(inp: Array[Byte]) = {
    val handler = new Handler()
    val inStream = new ByteArrayInputStream(inp)
    val outStream = new ByteArrayOutputStream()

    handler.handle(inStream, outStream, getContext)

    outStream.toByteArray
  }

  def getContext = new Context {
    override def getFunctionName = ???
    override def getRemainingTimeInMillis = ???
    override def getLogger = ???
    override def getFunctionVersion = ???
    override def getMemoryLimitInMB = ???
    override def getClientContext = ???
    override def getLogStreamName = ???
    override def getInvokedFunctionArn = ???
    override def getIdentity = ???
    override def getLogGroupName = ???
    override def getAwsRequestId = ???
  }

}
