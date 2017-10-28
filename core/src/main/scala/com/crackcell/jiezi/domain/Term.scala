package com.crackcell.jiezi.domain

/**
  * Term
  *
  * @author Menglong TAN
  */
case class Term(var word: String, var pos: POSArray, var offset: Int, var frequency: Long)
  extends Serializable {

  def length: Int = word.length

  def this(term: Term) = this(term.word, term.pos, term.offset, term.frequency)

  def this(word: String, pos: Array[String], offset: Int = 0, frequency: Long = 0) =
    this(word, new POSArray(pos.map(new POS(_))), offset, frequency)

  def ==(other: Term): Boolean = other.word == word && other.pos == pos

  def setWord(value: String): this.type = {
    word = value
    this
  }

  def setPOS(value: POSArray): this.type = {
    pos = value
    this
  }

  def setFrequency(value: Long): this.type = {
    frequency = value
    this
  }

  def setOffset(value: Int): this.type = {
    offset = value
    this
  }

  override def toString: String = s"${word} [pos: ${pos}] [offset: ${offset}] [freq: ${frequency}]"
}
