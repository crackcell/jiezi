package com.crackcell.jiezi.dict.loader.io

import java.io.InputStream

/**
  * 路径转流接口
  *
  * @author Menglong TAN
  */
trait ToStream[S] extends Serializable {
  def toStream(source: S): InputStream
}
