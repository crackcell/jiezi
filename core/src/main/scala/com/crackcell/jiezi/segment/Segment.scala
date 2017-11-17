package com.crackcell.jiezi.segment

/**
  * 分词接口
  *
  * @author Menglong TAN
  */
trait Segment extends Serializable {
  def parse(query: String): Result
}
