package com.crackcell.jiezi.dict.loader

import com.crackcell.jiezi.WordsegException
import com.crackcell.jiezi.dict.TermDict
import com.crackcell.jiezi.domain.{Nature, Term}

/**
  * TermDict加载器
  *
  * @author Menglong TAN
  */
class TermDictLoader[S](val tostream: ToStream[S]) extends DictLoader[TermDict, S](tostream) {

  override protected def newDict = new TermDict()

  override protected def parseLine(line: String, dict: TermDict): Unit = {
    val newLine = line.trim.toLowerCase()
    if (newLine.length == 0) return
    val tokens = newLine.split("\t")
    if (tokens.length != 3) {
      throw new WordsegException(s"invalid line: ${line}")
    }

    dict.put(
      new Term(
        word = tokens(0).trim.toLowerCase(),
        natures = tokens(1).split(",").map(token => new Nature(nature = token.trim)),
        frequency = tokens(2).trim.toLong
      )
    )
  }
}
