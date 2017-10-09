package com.crackcell.jiezi.domain

/**
  * Term
  *
  * @author Menglong TAN
  */
class Term(word: String = "", natures: Array[Nature] = Array[Nature](), frequency : Long = 0) {

  def getWord() = word

  override def toString: String = s"${word} [n: ${natures.mkString(",")}] [f: ${frequency}]"
}
