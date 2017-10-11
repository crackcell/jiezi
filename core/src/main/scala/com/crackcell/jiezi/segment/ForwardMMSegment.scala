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
    var offset = 0
    var start = -1
    var isEnOrM = 'u'
    var term: Option[Term] = None
    var word: String = null
    val terms = new ArrayBuffer[Term]()
    while (i < query.length) {
      val ch = query.charAt(i)
      word = String.valueOf(ch)
      if (StringUtils.isEnglish(ch)) {
        if (start == -1) {
          start = i
          isEnOrM = 'e'
        } else if (isEnOrM == 'm') {
          if (offset != start) throw new WordsegException("imcompleted wordseg")
          terms.append(new Term(word = query.substring(start, i), natures = Array(new Nature("n"))))
          start = i
          offset = i
          isEnOrM = 'e'
        } else if (isEnOrM != 'e') throw new WordsegException("invalid stat")
      } else if (StringUtils.isNumber(ch) || start == -1 && StringUtils.isOperator(ch)) {
        if (start == -1) {
          start = i
          isEnOrM = 'm'
        } else if (isEnOrM == 'e') {
          if (offset != start) throw new WordsegException("imcompleted wordseg")
          terms.append(new Term(word = query.substring(start, i), natures = Array(new Nature("e"))))
          start = i
          offset = i
          isEnOrM = 'e'
        } else if (isEnOrM != 'm') throw new WordsegException("invalid stat")
      } else {
        if (isStartChar(ch)) {
          term = findLongestHeadWord(query.substring(i, query.length))
          if (term.isDefined) {
            word = term.get.getWord()
            if (start != -1) {
              val nature =
                if (StringUtils.isEnglish(query.charAt(start))) "en"
                else if (StringUtils.isDigit(query.charAt(start)) || '.' == query.charAt(start)) "m"
                else throw new WordsegException("invalid stat")
              if (offset != start) throw new WordsegException("imcompleted wordseg")
              terms.append(new Term(word = query.substring(start, i), natures = Array(new Nature(nature))))
              start = -1
              offset = i
            }
            if (offset != i) throw new WordsegException("imcompleted wordseg")
            terms.append(term.get)
            offset = i + term.get.getWord().length
          }
        }
      }
      i = i + word.length
    }

    if (start != -1) {
      val nature =
        if (StringUtils.isEnglish(query.charAt(start)))
          "en"
        else if (StringUtils.isNumber(query.charAt(start)))
          "m"
        else throw new WordsegException("invalid stat")
      if (offset != start) throw new WordsegException("imcompleted wordseg")
      terms.append(new Term(word = query.substring(start, i - 1), natures = Array(new Nature(nature))))
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
