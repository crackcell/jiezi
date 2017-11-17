package com.crackcell.jiezi.segment

import com.crackcell.jiezi.domain.Result

/**
  * 分词接口
  *
  * @author Menglong TAN
  */
trait Segment extends Serializable {
  def parse(query: String): Result
}
