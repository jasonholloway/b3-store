package com.woodpigeon.b3

import java.io.InputStream

import scala.concurrent.Future

class Sink {

  def commit(updates: InputStream): Future[Unit] = {
    Future.unit
  }

  def flush(): Future[Unit] = {
    Future.unit
  }

  println("heyup")
}

//it'd be problematic to expect json-serialized updates into the data store...
//as protobuf doesn't really like such things... we'd be victims of scala.js's
//in-built serializer. But if we dealt in protobuf serializations, then there'd be standardization.
//so the data thing'd be best wrapped on the typescript side. This'd also solve the problem of communicating
//types outwards - the interface would just be the protocol.

//The wrapper in JS would receive protobuffable objects, convert them to byte arrays or streams
//This would be an entirely client-side concern. Then the wrapper 'server'-side would deserialize, and apply to the
//inner data.
//