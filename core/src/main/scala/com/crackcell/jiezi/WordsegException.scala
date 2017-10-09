package com.crackcell.jiezi

import java.io.Serializable

/**
  * 分词异常
  */
class WordsegException(message: String, cause: Throwable) extends RuntimeException(message, cause) with Serializable {
  def this(message: String) = this(message, null)
  def this(cause: Throwable) = this(null, cause)
}