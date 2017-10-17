package com.crackcell.jiezi.dict.parser

import java.io.InputStream

/**
  * 路径转流接口
  *
  * @author Menglong TAN
  */
trait ToStream[S] extends Serializable {
  def toStream(source: S): InputStream
}
