package com.crackcell.jiezi.segment

import com.crackcell.jiezi.dict.loader.{FileToStream, TermDictLoader}
import org.scalatest.FunSuite

/**
  * 最大正向匹配分词
  *
  * @author Menglong TAN
  */
class ForwardMMSegmentTest extends FunSuite {

  val dictPathRoot = this.getClass.getClassLoader.getResource("").getPath + "../../../dist/dict/"

  val loader = new TermDictLoader(new FileToStream)
  val coreDict = loader.loadDict(dictPathRoot + "/core_term.dict")
  val segment = new ForwardMMSegment(coreDict)

  test("parse") {
    segment.parse(" 新iphonesp全新").terms.foreach(println)
  }

  test("parse number and english char") {
    segment.parse("+100.01sp198+109non-smoking").terms.foreach(println)
  }

}
