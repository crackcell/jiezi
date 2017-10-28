package com.crackcell.jiezi.domain

/**
  * 词性
  *
  * @author Menglong TAN
  */
case class POS(pos: String = "unknown", frequency: Long = 0) extends Serializable {
  def ==(other: POS): Boolean = other.pos == pos

  def !=(other: POS): Boolean = !(==(other))

  override def toString: String = pos
}
