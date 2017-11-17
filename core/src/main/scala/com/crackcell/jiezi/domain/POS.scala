package com.crackcell.jiezi.domain

import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._

/**
  * 词性
  *
  * @author Menglong TAN
  */
case class POS(pos: String = "unknown", frequency: Long = 0) extends Serializable with ToJson {
  def ==(other: POS): Boolean = other.pos == pos

  def !=(other: POS): Boolean = !(==(other))

  override def toString: String = pos

  override def jsonValue: JValue = ("pos" -> pos) ~ ("freq" -> frequency)
}
