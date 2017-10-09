package com.crackcell.jiezi.dict.parser

import java.io.InputStream

/**
  * 路径转流接口
  *
  * @author Menglong TAN
  */
trait PathToStream extends Serializable {
  def toStream(path: String): InputStream
}
