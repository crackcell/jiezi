package com.crackcell.jiezi.domain

case class POSArray(data: Array[POS] = Array()) extends Iterable[POS] {

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
}
