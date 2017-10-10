package com.crackcell.jiezi.dict

import com.crackcell.jiezi.domain.Term
import com.crackcell.jiezi.util.RadixTree

/**
  * Term字典
  *
  * @author Menglong TAN
  */
class TermDict(val data: RadixTree[Term] = new RadixTree[Term]) {

  def put(term: Term): Unit = {
    data.put(term.getWord, term)
  }

  def isStartChar(ch: Char): Boolean = data.isStartChar(ch)

  def isMiddleChar(ch: Char): Boolean = data.isMiddleChar(ch)

  def isEndChar(ch: Char): Boolean = data.isEndChar(ch)

  def findPrefixWords(prefix: String): Seq[Term] = data.findCommonPrefixWords(prefix).values.toSeq

  def findLongestPrefixWord(query: String): Option[Term] = {
    val result = data.findLongestPrefixWord(query)
    if (result.isEmpty) return None
    return Some(result.get._2)
  }
}
