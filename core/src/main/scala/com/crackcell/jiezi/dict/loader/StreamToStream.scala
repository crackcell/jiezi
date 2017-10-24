package com.crackcell.jiezi.dict.loader

import java.io.InputStream

/**
  * InputStream
  *
  * @author Menglong TAN
  */
class StreamToStream extends ToStream[InputStream] {
  override def toStream(source: InputStream) = source
}
