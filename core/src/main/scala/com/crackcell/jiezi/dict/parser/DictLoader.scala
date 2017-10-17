package com.crackcell.jiezi.dict.parser

import java.io.{BufferedReader, InputStreamReader}

import com.crackcell.jiezi.WordsegException

/**
  * 词典加载接口
  *
  * @author Menglong TAN
  */
abstract class DictLoader[D, S](val toStream: ToStream[S]) extends Serializable {

  def loadDict(source: S): D = {
    val dict = newDict
    val br = new BufferedReader(new InputStreamReader(toStream.toStream(source)))
    try {
      var line = br.readLine()
      while (line != null) {
        parseLine(line, dict)
        line = br.readLine()
      }
    } catch {
      case e: Exception => throw new WordsegException(e)
    } finally if (br != null) br.close()

    dict
  }

  protected def newDict: D

  protected def parseLine(line: String, dict: D)
}
