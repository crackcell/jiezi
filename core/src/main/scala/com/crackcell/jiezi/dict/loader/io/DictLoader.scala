package com.crackcell.jiezi.dict.loader.io

import java.io.{BufferedReader, InputStreamReader}

import com.crackcell.jiezi.WordsegException
import com.crackcell.jiezi.dict.loader.io.ToStream
import com.crackcell.jiezi.dict.loader.io.io.ToStream

/**
  * 词典加载接口
  *
  * @author Menglong TAN
  */
abstract class DictLoader[D, S] extends Serializable {

  def toStream(): ToStream[S]

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
