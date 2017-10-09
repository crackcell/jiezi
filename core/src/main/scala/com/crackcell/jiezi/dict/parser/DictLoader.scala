package com.crackcell.jiezi.dict.parser

import java.io.{BufferedReader, InputStreamReader}

import com.crackcell.jiezi.WordsegException

/**
  * 词典加载接口
  *
  * @author Menglong TAN
  */
abstract class DictLoader[D](val path2stream: PathToStream) extends Serializable {

  def loadDict(location: String): D = {
    val dict = newDict
    val br = new BufferedReader(new InputStreamReader(path2stream.toStream(location)))
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
