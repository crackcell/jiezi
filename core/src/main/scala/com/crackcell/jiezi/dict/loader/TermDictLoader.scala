package com.crackcell.jiezi.dict.loader

import com.crackcell.jiezi.WordsegException
import com.crackcell.jiezi.dict.TermDict
import com.crackcell.jiezi.dict.loader.io.ToStream
import com.crackcell.jiezi.domain.{POS, POSArray, Term}

/**
  * TermDict加载器
  *
  * @author Menglong TAN
  */
class TermDictLoader[S](override val toStream: ToStream[S]) extends DictLoader[TermDict, S] {

  override protected def newDict = new TermDict()

  override protected def parseLine(line: String, dict: TermDict): Unit = {
    val tokens = line.toLowerCase.split("\t")
    if (tokens.length != 3) {
      throw new WordsegException(s"invalid line: ${line}")
    }

    dict.put(
      new Term(
        word = tokens(0).toLowerCase(),
        pos = tokens(1).split(",").map(_.trim),
        frequency = tokens(2).trim.toLong
      )
    )
  }
}
