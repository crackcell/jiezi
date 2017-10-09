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
    val terms = new ArrayBuffer[Term]()
    val enBuff = new StringBuffer()
    val numBuff = new StringBuffer()
    while (i < query.length) {
      var term: Option[Term] = None
      var ch = query.charAt(i)

      // 基于字典查找
      if (isStartChar(ch)) {
        term = findLongestHeadWord(query.substring(i, query.length))
        if (term.isDefined) {
          if (term.get.getWord().length == 1 && StringUtils.isEnglishWord(term.get.getWord().charAt(0))) {
            term = None
          } else if (term.get.getWord().length == 1 && StringUtils.isNumber(term.get.getWord().charAt(0))) {
            term = None
          } else {
            i = i + term.get.getWord().length
            terms.append(term.get)
            if (enBuff.length() != 0) {
              terms.append(new Term(word = enBuff.toString, natures = Array(new Nature("en"))))
              enBuff.delete(0, enBuff.length())
            }
            if (numBuff.length() != 0) {
              terms.append(new Term(word = numBuff.toString, natures = Array(new Nature("m"))))
              numBuff.delete(0, numBuff.length())
            }
          }
        }
      }

      // 基于字典搜索失败，尝试使用策略补全term
      if (term.isEmpty) {
        if (StringUtils.isEnglishWord(ch)) {
          enBuff.append(ch)
          if (numBuff.length() != 0) {
            terms.append(new Term(word = numBuff.toString, natures = Array(new Nature("m"))))
            numBuff.delete(0, numBuff.length())
          }
        } else if (StringUtils.isNumber(ch)) {
          numBuff.append(ch)
          if (enBuff.length() != 0) {
            terms.append(new Term(word = enBuff.toString, natures = Array(new Nature("en"))))
            enBuff.delete(0, enBuff.length())
          }
        }
        i = i + 1
      }
    }

    if (enBuff.length() != 0) terms.append(new Term(word = enBuff.toString, natures = Array(new Nature("en"))))
    else if (numBuff.length() != 0) terms.append(new Term(word = numBuff.toString, natures = Array(new Nature("m"))))

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
