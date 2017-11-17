package com.crackcell.jiezi.segment

import com.crackcell.jiezi.WordsegException
import com.crackcell.jiezi.dict.{StopDict, TermDict}
import com.crackcell.jiezi.domain.Term
import com.crackcell.jiezi.util.StringUtils

import scala.collection.mutable.ArrayBuffer

/**
  * 正向最大匹配分词
  *
  * @author Menglong TAN
  */
class ForwardMMSegment(var termDicts: Array[TermDict] = Array(),
                       var stopDict: StopDict = new StopDict(),
                       var handleInvalid: String = "skip")
  extends Segment {

  private val OUTPUT_STATE = 0
  private val DETECT_STATE = 1
  private val EN_STATE = 2
  private val NUM_STATE = 3

  private val UNKNOWN_ACTION = 0
  private val SKIP_ACTION = 1
  private val TERM_ACTION = 2
  private val EN_ACTION = 3
  private val NUM_ACTION = 4

  def setTermDicts(value: TermDict*): this.type = setTermDicts(value.toArray)

  def setTermDicts(value: Array[TermDict]): this.type = {
    termDicts = value.reverse
    this
  }

  def setStopDict(value: StopDict): this.type = {
    stopDict = value
    this
  }

  def setHandleInvalid(value: String): this.type = {
    handleInvalid = value
    this
  }

  override def parse(query: String): Result = {
    val q = query.toLowerCase
    val qlen = q.length
    val terms = new ArrayBuffer[Term]()

    var offset = 0
    var ch = '*'
    var state = DETECT_STATE
    var detectedCharType = UNKNOWN_ACTION
    var term: Option[Term] = None
    var skipStep = -1

    var buffPos = "?"
    var buffOffset = 0

    while (offset < qlen) {

      state match {

        case DETECT_STATE =>
          ch = q.charAt(offset)

          if (StringUtils.isDelimiter(ch) || stopDict.isStopWord(ch)) {
            skipStep = 1
            detectedCharType = SKIP_ACTION
            state = OUTPUT_STATE
          } else if (isStartChar(ch)) {
            term = findLongestHeadWord(q.substring(offset, qlen))
            if (term.isEmpty && StringUtils.isEnglish(ch)) {
              state = EN_STATE
            } else if (term.isEmpty && StringUtils.isNumber(ch)) {
              state = NUM_STATE
            } else if (term.isEmpty) {
              if (handleInvalid == "skip") {
                skipStep = 1
                detectedCharType = SKIP_ACTION
                state = OUTPUT_STATE
              } else {
                throw new WordsegException(s"invalid char: ${ch}")
              }
            } else if (stopDict.isStopTerm(term.get)) {
              skipStep = term.get.word.length
              term = None
              detectedCharType = SKIP_ACTION
              state = OUTPUT_STATE
            } else {
              term = Some(term.get.setOffset(offset))
              detectedCharType = TERM_ACTION
              state = OUTPUT_STATE
            }
          } else if (StringUtils.isEnglish(ch)) {
            state = EN_STATE
          } else if (StringUtils.isNumber(ch)) {
            state = NUM_STATE
          } else if (handleInvalid == "skip") {
            skipStep = 1
            detectedCharType = SKIP_ACTION
            state = OUTPUT_STATE
          } else {
            throw new WordsegException(s"invalid char: ${ch}")
          }

        case EN_STATE =>
          detectedCharType = EN_ACTION
          if (buffOffset < offset && buffPos != "en") {
            state = OUTPUT_STATE
          } else {
            buffPos = "en"
            offset = offset + 1
            state = DETECT_STATE
          }

        case NUM_STATE =>
          detectedCharType = NUM_ACTION
          if (buffOffset < offset && buffPos != "m") {
            state = OUTPUT_STATE
          } else {
            buffPos = "m"
            offset = offset + 1
            state = DETECT_STATE
          }

        case OUTPUT_STATE =>
          detectedCharType match {

            case SKIP_ACTION =>
              if (buffOffset < offset) {
                terms.append(new Term(
                  word = q.substring(buffOffset, offset),
                  pos = Array(buffPos),
                  offset = buffOffset
                ))
              } else if (term.isDefined) {
                terms.append(new Term(term.get))
              }
              offset = offset + skipStep
              buffOffset = offset
              buffPos = "?"

            case TERM_ACTION =>
              if (buffOffset < offset) {
                terms.append(new Term(
                  word = q.substring(buffOffset, offset),
                  pos = Array(buffPos),
                  offset = buffOffset
                ))
              }
              terms.append(new Term(term.get))
              offset = offset + term.get.word.length
              term = None
              buffOffset = offset
              buffPos = "?"

            case EN_ACTION =>
              if (buffOffset < offset && buffPos != "en") {
                terms.append(new Term(
                  word = q.substring(buffOffset, offset),
                  pos = Array(buffPos),
                  offset = buffOffset
                ))
                buffOffset = offset
                buffPos = "en"
              } else {
                throw new WordsegException("invalid state")
              }

            case NUM_ACTION =>
              if (buffOffset < offset && buffPos != "m") {
                terms.append(new Term(
                  word = q.substring(buffOffset, offset),
                  pos = Array(buffPos),
                  offset = buffOffset
                ))
                buffOffset = offset
                buffPos = "m"
              } else {
                throw new WordsegException("invalid state")
              }

            case action =>
              throw new WordsegException(s"invalid action: ${action}")
          }
          state = DETECT_STATE
        case _ => throw new WordsegException(s"invalid state: ${state}")
      }

      if (offset == qlen) {
        if (buffOffset < offset) {
          terms.append(new Term(
            word = q.substring(buffOffset, qlen),
            pos = Array(buffPos),
            offset = buffOffset
          ))
        } else if (term.isDefined) {
          terms.append(new Term(term.get))
        }
      }

    }

    Result(query, terms.toArray)
  }

  protected def containsChar(ch: Char): Boolean = {
    for (dict <- termDicts) if (dict.containsChar(ch)) return true
    false
  }

  protected def isStartChar(ch: Char): Boolean = {
    for (dict <- termDicts) {
      if (dict.isStartChar(ch)) {
        return true
      }
    }
    false
  }

  protected def isEndChar(ch: Char): Boolean = {
    for (dict <- termDicts) if (dict.isEndChar(ch)) return true
    false
  }

  protected def findLongestHeadWord(query: String): Option[Term] = {
    var maxlen = 0
    var term: Option[Term] = None
    for (dict <- termDicts) {
      val t = dict.findLongestPrefixWord(query)
      if (t.isDefined && t.get.word.length > maxlen) {
        maxlen = t.get.word.length
        term = t
      }
    }
    term
  }

}
