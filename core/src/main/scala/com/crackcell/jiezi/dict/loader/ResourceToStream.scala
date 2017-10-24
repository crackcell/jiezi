package com.crackcell.jiezi.dict.loader

/**
  * 从Jar包加载词典
  *
  * 路径格式：jar://包名称|/资源路径
  *
  * @author Menglong TAN
  */
class ResourceToStream extends ToStream[String] {
  override def toStream(path: String) = {
    this.getClass.getResourceAsStream(path)
  }
}
