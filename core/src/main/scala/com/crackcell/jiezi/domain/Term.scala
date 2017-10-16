package com.crackcell.jiezi.domain

/**
  * Term
  *
  * @author Menglong TAN
  */
class Term(word: String = "", natures: Array[Nature] = Array[Nature](), frequency : Long = 0) extends Serializable {

  def this(word: String, nature: Nature) = this(word, Array(nature))

  def this(word: String, nature: String) = this(word, new Nature(nature))

  def getWord() = word

  def getNatures() = natures

  override def toString: String = s"${word} [n: ${natures.mkString(",")}] [f: ${frequency}]"
}
