package com.crackcell.jiezi.segment

import com.crackcell.jiezi.dict.parser.{FileToStream, TermDictLoader}
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

  /*
  test("parse") {
    segment.parse("新iphonesp").getTerms().foreach(println)
  }

  test("parse number and english char") {
    segment.parse("+100.01sp198+109non-smoking").getTerms().foreach(println)
  }
  */

  test("parse number and english char") {
    segment.parse("+1s1").getTerms().foreach(println)
  }

}
