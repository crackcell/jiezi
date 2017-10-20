package com.crackcell.jiezi.segment

import com.crackcell.jiezi.WordsegException
import com.crackcell.jiezi.dict.TermDict
import com.crackcell.jiezi.domain.{Result, Term}
import com.crackcell.jiezi.util.StringUtils

import scala.collection.mutable.ArrayBuffer

/**
  * 正向最大匹配分词
  *
  * @author Menglong TAN
  */
class ForwardMMSegment(var handleInvalid: String, val dicts: TermDict*) extends Segment {

  def this(dicts: TermDict*) = this("error", dicts: _*)

  def setHandleInvalid(handleInvalid: String): ForwardMMSegment = {
    this.handleInvalid = handleInvalid
    this
  }

  override def parse(q: String): Result = {
    val query = q.toLowerCase()
    val terms = new ArrayBuffer[Term]()

    // state:
    // 1: try dict
    // 2: eng
    // 3: num
    var offset = 0
    var ch = '*'
    var word: String = null
    var state = 1
    var lastSpecialCharType = '*'
    var newTermOffset = 0
    var term: Option[Term] = None

    while (offset < query.length) {
      ch = query.charAt(offset)

      state match {
        case 1 =>
          if (!containsChar(ch)) { // 在字典里面没出现过的字符，跳过
            offset = offset + 1
            newTermOffset = offset
            lastSpecialCharType = '*'
          } else {

            term = findLongestHeadWord(query.substring(offset, query.length))
            if (term.isEmpty) {
              handleInvalid match {
                case "error" => throw new WordsegException(s"incomplete wordseg: query=${query}, offset=${offset}")
                case "skip" =>
                  offset = offset + 1
                  newTermOffset = offset
                  lastSpecialCharType = '*'
              }
            } else {
              term = Some(new Term(term.get))
              word = term.get.word
              val firstCh = word.charAt(0)
              if (word.length == 1 && StringUtils.isEnglish(firstCh)) state = 2
              else if (word.length == 1 && StringUtils.isNumber(firstCh)) state = 3
              else {
                if (newTermOffset < offset) {
                  val nature =
                    if (lastSpecialCharType == 'e') "en"
                    else if (lastSpecialCharType == 'm') "m"
                    else ""
                  terms.append(new Term(query.substring(newTermOffset, offset), nature))
                  newTermOffset = offset
                }
                terms.append(term.get)
                offset = offset + word.length
                newTermOffset = offset
              }
            }

          }
        case 2 =>
          if (lastSpecialCharType == 'm') {
            terms.append(new Term(query.substring(newTermOffset, offset), "m"))
            newTermOffset = offset
          }
          if (offset == query.length - 1) {
            terms.append(new Term(query.substring(newTermOffset, offset + 1), "en"))
          }
          state = 1
          lastSpecialCharType = 'e'
          offset = offset + 1
        case 3 =>
          if (lastSpecialCharType == 'e') {
            terms.append(new Term(query.substring(newTermOffset, offset), "en"))
            newTermOffset = offset
          }
          if (offset == query.length - 1) {
            terms.append(new Term(query.substring(newTermOffset, offset + 1), "m"))
          }
          state = 1
          lastSpecialCharType = 'm'
          offset = offset + 1
      }

    }

    new Result(terms = terms.toArray)
  }

  protected def containsChar(ch: Char): Boolean = {
    for (dict <- dicts) if (dict.containsChar(ch)) return true
    false
  }

  protected def isStartChar(ch: Char): Boolean = {
    for (dict <- dicts) if (dict.isStartChar(ch)) return true
    false
  }

  protected def isEndChar(ch: Char): Boolean = {
    for (dict <- dicts) if (dict.isEndChar(ch)) return true
    false
  }

  protected def findLongestHeadWord(query: String): Option[Term] = {
    var maxlen = 0
    var term: Option[Term] = None
    for (dict <- dicts) {
      val t = dict.findLongestPrefixWord(query)
      if (t.isDefined && t.get.word.length > maxlen) {
        maxlen = t.get.word.length
        term = t
      }
    }
    term
  }

}
