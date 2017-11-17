package com.crackcell.jiezi.domain

import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._

case class POSArray(data: Array[POS] = Array()) extends Iterable[POS] with ToJson {

  def this(pos: String*) = this(pos.map(POS(_)).toArray)

  def length: Int = data.length

  def contains(pos: POS): Boolean = data.contains(pos)

  def ==(other: POSArray): Boolean = {
    if (other.length != length) {
      return false
    }
    for (pos <- other) {
      if (!contains(pos)) {
        return false
      }
    }
    true
  }

  def !=(other: POSArray): Boolean = !(==(other))

  override def toString: String = data.mkString(",")

  override def iterator = data.iterator

  override def jsonValue: JValue = data.map(_.jsonValue).toList
}
