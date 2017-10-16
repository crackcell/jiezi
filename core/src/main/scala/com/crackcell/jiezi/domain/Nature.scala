package com.crackcell.jiezi.domain

/**
  * 词性
  *
  * @author Menglong TAN
  */
class Nature(nature: String = "unknown", frequency: Long = 0) extends Serializable {
  override def toString: String = nature
}
