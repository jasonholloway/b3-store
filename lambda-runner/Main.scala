import java.io.{BufferedReader, ByteArrayInputStream, ByteArrayOutputStream, InputStreamReader}
import com.amazonaws.services.lambda.runtime.Context
import woodpigeon.bb.store.Handler

object Main extends App {
  val streamReader = new InputStreamReader(System.in)
  val reader = new BufferedReader(streamReader)

  reader.lines().forEach(l => {
    val context = createContext("myFunction")
    val handler = new Handler()

    val input = new ByteArrayInputStream(l.getBytes("UTF-8"))
    val output = new ByteArrayOutputStream(4096)

    try {
      handler.handle(input, output, context)
      output.write('\n')
      output.writeTo(System.out)
      System.out.println()
    }
    catch {
      case ex => {
        System.err.println(ex)
        System.out.println(s"[ERR] $ex")
      }
    }
    finally {
      output.close
//      input.close
    }
  })

  def createContext(name: String) = new Context() {
    override def getFunctionName = name
    override def getRemainingTimeInMillis = ???
    override def getLogger = ???
    override def getFunctionVersion = "1.0.0"
    override def getMemoryLimitInMB = ???
    override def getClientContext = ???
    override def getLogStreamName = ???
    override def getInvokedFunctionArn = ???
    override def getIdentity = ???
    override def getLogGroupName = ???
    override def getAwsRequestId = ???
  }

}
