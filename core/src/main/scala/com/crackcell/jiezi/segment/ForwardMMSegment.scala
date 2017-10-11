package com.crackcell.jiezi.segment

import com.crackcell.jiezi.WordsegException
import com.crackcell.jiezi.dict.TermDict
import com.crackcell.jiezi.domain.{Nature, Result, Term}
import com.crackcell.jiezi.util.StringUtils

import scala.collection.mutable.ArrayBuffer

/**
  * 正向最大匹配分词
  *
  * @author Menglong TAN
  */
class ForwardMMSegment(val dicts: TermDict*) extends Segment {

  override def parse(query: String): Result = {
    var i = 0
    var start = -1
    var end = -1
    var term: Option[Term] = None
    var word: String = null
    val terms = new ArrayBuffer[Term]()
    while (i < query.length) {
      val ch = query.charAt(i)
      word = String.valueOf(ch)
      if (isStartChar(ch)) {
        term = findLongestHeadWord(query.substring(i, query.length))
        if (term.isDefined) {
          word = term.get.getWord()
          if (word.length == 1 &&
              (StringUtils.isEnglishWord(word.charAt(0)) || StringUtils.isDigit(word.charAt(0)))) {
            term = None
            if (start == -1)
              start = i
            else
              end = i
          } else {
            terms.append(term.get)
            if (start != -1) {
              val nature =
                if (StringUtils.isEnglishWord(query.charAt(start))) "en"
                else if (StringUtils.isNumber(query.charAt(start))) "m"
                else throw new WordsegException("invalid stat")
              terms.append(new Term(word = query.substring(start, end + 1), natures = Array(new Nature(nature))))
              start = -1
              end = -1
            }
          }
        }
      }
      i = i + word.length
    }

    if (start != -1) {
      val nature =
        if (StringUtils.isEnglishWord(query.charAt(start)))
          "en"
        else if (StringUtils.isNumber(query.charAt(start)))
          "m"
        else throw new WordsegException("invalid stat")
      terms.append(new Term(word = query.substring(start, end + 1), natures = Array(new Nature(nature))))
    }

    new Result(terms = terms.toArray)
  }

  protected def isStartChar(ch: Char): Boolean = {
    for (dict <- dicts) if (dict.isStartChar(ch)) return true
    return false
  }

  protected def findLongestHeadWord(query: String): Option[Term] = {
    var maxlen = 0
    var term: Option[Term] = None
    for (dict <- dicts) {
      val t = dict.findLongestPrefixWord(query)
      if (t.isDefined && t.get.getWord().length > maxlen) {
        maxlen = t.get.getWord().length
        term = t
      }
    }
    term
  }

  protected def isEndChar(ch: Char): Boolean = {
    for (dict <- dicts) if (dict.isEndChar(ch)) return true
    return false
  }

}
