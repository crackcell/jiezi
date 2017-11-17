package com.crackcell.jiezi.dict.loader.io

import java.util.regex.Pattern

import com.crackcell.jiezi.WordsegException
import com.crackcell.jiezi.dict.StopDict
import com.crackcell.jiezi.dict.loader.io.ToStream
import com.crackcell.jiezi.dict.loader.io.io.ToStream

class StopDictLoader[S](override val toStream: ToStream[S]) extends DictLoader [StopDict, S] {

  override protected def newDict: StopDict = new StopDict()

  override protected def parseLine(line: String, dict: StopDict): Unit = {
    val tokens = line.toLowerCase.split("\t")
    if (tokens.length != 2) {
      throw new WordsegException(s"invalid line: ${line}")
    }

    val key = tokens(0)
    val typ = tokens(1)
    typ match {
      case "keyword" => dict.stopword.add(key)
      case "pos" => dict.stoppos.add(key)
      case "regex" => dict.stopregex.add(Pattern.compile(key))
      case _ => throw new WordsegException(s"invalid type: ${typ}")
    }
  }
}
