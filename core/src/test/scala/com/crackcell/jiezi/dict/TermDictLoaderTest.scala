package com.crackcell.jiezi.dict

import com.crackcell.jiezi.dict.loader.TermDictLoader
import com.crackcell.jiezi.dict.loader.io.FileToStream
import org.scalatest.FunSuite

/**
  * Created by Menglong TAN on 10/9/17.
  */
class TermDictLoaderTest extends FunSuite {
  val dictPathRoot = this.getClass.getClassLoader.getResource("").getPath + "../../../dist/dict/"

  test("load") {
    val loader = new TermDictLoader(new FileToStream)
    val dict = loader.loadDict(dictPathRoot + "/core_term.dict")
    dict.findLongestPrefixWord("我是中国人").foreach(println)
  }
}
