package com.crackcell.jiezi.dict

import java.util.regex.Pattern

import com.crackcell.jiezi.domain.Term

import scala.collection.mutable

/**
  * 停用词词典
  *
  * @author Menglong TAN
  */
class StopDict(val stopword: mutable.Set[String] = new mutable.HashSet[String](),
               val stoppos: mutable.Set[String] = new mutable.HashSet[String](),
               val stopregex: mutable.Set[Pattern] = new mutable.HashSet[Pattern]())
  extends Serializable {

  def putWord(word: String): this.type = {
    stopword.add(word)
    this
  }

  def putPOS(pos: String): this.type = {
    stoppos.add(pos)
    this
  }

  def putRegex(regex: String): this.type = {
    stopregex.add(Pattern.compile(regex))
    this
  }

  def isStopWord(word: Char): Boolean = isStopWord(String.valueOf(word))

  def isStopWord(word: String): Boolean = {
    if (stopword.contains(word)) return true
    for (p <- stopregex) if (p.matcher(word).matches()) return true
    false
  }

  def isStopPOS(pos: String): Boolean = stoppos.contains(pos)

  def isStopRegex(word: String): Boolean = {
    for (p <- stopregex) if (p.matcher(word).matches()) return true
    false
  }

  def isStopTerm(term: Term): Boolean = {
    if (isStopWord(term.word)) return true
    for (pos <- term.pos) if (isStopPOS(pos.pos)) return true
    false
  }
}
