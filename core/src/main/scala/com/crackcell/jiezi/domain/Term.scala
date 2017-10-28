package com.crackcell.jiezi.domain

/**
  * Term
  *
  * @author Menglong TAN
  */
case class Term(word: String = "", pos: POSArray = new POSArray, frequency: Long = 0) extends Serializable {

  def this(term: Term) = this(term.word, term.pos, term.frequency)

  def this(word: String, pos: POS) = this(word, new POSArray(Array(pos)))

  def this(word: String, pos: String) = this(word, new POS(pos))

  override def toString: String = s"${word} [pos: ${pos}] [freq: ${frequency}]"

  def ==(other: Term): Boolean = other.word == word && other.pos == pos
}
