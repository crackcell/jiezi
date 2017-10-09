package com.crackcell.jiezi.dict

import com.crackcell.jiezi.domain.Term
import com.crackcell.jiezi.util.RadixTree

/**
  * Term字典
  *
  * @author Menglong TAN
  */
class TermDict(val data: RadixTree[Term] = new RadixTree[Term]) extends Dict {

  override def newDict = new TermDict()

  def put(term: Term): Unit = {
    data.put(term.getWord, term)
  }

  def findPrefixWords(prefix: String): Seq[Term] = data.findPrefixWords(prefix).values.toSeq

  def findLongestHeadWord(query: String): Option[Term] = Option(data.findLongestHeadWord(query).get._2)
}
