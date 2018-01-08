package com.crackcell.jiezi.domain

import org.json4s.JsonAST.JValue
import org.json4s.JsonDSL._

/**
  * Immutable Term data, every change will get a new Term object.
  *
  * @author Menglong TAN
  */
case class Term(var word: String, var pos: POSArray, var offset: Int, var frequency: Long)
  extends Serializable with ToJson {

  def length: Int = word.length

  def this(term: Term) = this(term.word, term.pos, term.offset, term.frequency)

  def this(word: String, pos: Array[String], offset: Int = 0, frequency: Long = 0) =
    this(word, new POSArray(pos.map(new POS(_))), offset, frequency)

  def ==(other: Term): Boolean = other.word == word && other.pos == pos

  def setWord(value: String): Term = {
    val obj = clone()
    obj.word = value
    obj
  }

  def setPOS(value: POSArray): Term = {
    val obj = clone()
    obj.pos = value
    obj
  }

  def setFrequency(value: Long): Term = {
    val obj = clone()
    obj.frequency = value
    obj
  }

  def setOffset(value: Int): Term = {
    val obj = clone()
    obj.offset = value
    obj
  }

  override def clone(): Term = new Term(this)

  override def toString: String = s"${word} [pos: ${pos}] [offset: ${offset}] [freq: ${frequency}]"

  override def jsonValue: JValue = ("word" -> word) ~ ("pos" -> pos.jsonValue) ~ ("offset" -> offset) ~ ("freq" -> frequency)
}
