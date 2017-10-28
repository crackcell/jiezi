package com.crackcell.jiezi.segment

import com.crackcell.jiezi.dict.loader.{FileToStream, StopDictLoader, TermDictLoader}
import com.crackcell.jiezi.domain.Term
import org.scalatest.FunSuite

/**
  * 最大正向匹配分词
  *
  * @author Menglong TAN
  */
class ForwardMMSegmentTest extends FunSuite {

  val dictPathRoot = this.getClass.getClassLoader.getResource("").getPath + "dict/"

  val termDictLoader = new TermDictLoader(new FileToStream)
  val stopDictLoader = new StopDictLoader(new FileToStream)

  val testcase = Array(
    (
      "core", "新全新",
      Array(
        new Term("新", Array("a", "an", "b", "d", "j", "ng", "nr", "v")),
        new Term("全新", Array("b", "d"))
      )
    ),
    (
      "core with sep", "新 好人",
      Array(
        new Term("新", Array("a", "an", "b", "d", "j", "ng", "nr", "v"))
      )
    ),
    (
      "en", "dfgdfg",
      Array(
        new Term("dfgdfg", Array("en"))
      )
    ),
    (
      "en with sep", "dfg exy",
      Array(
        new Term("dfg", Array("en")),
        new Term("exy", Array("en"))
      )
    ),
    (
      "m", "123",
      Array(
        new Term("123", Array("m"))
      )
    ),
    (
      "m with sep", "123 890",
      Array(
        new Term("123", Array("m")),
        new Term("890", Array("m"))
      )
    ),
    (
      "en with m with sep", "eng 890",
      Array(
        new Term("eng", Array("en")),
        new Term("890", Array("m"))
      )
    ),
    (
      "en with m with sep with term", "eng 890新",
      Array(
        new Term("eng", Array("en")),
        new Term("890", Array("m")),
        new Term("新", Array("a", "an", "b", "d", "j", "ng", "nr", "v"))
      )
    ),
    (
      "invalid char", "eng《890新",
      Array(
        new Term("eng", Array("en")),
        new Term("890", Array("m")),
        new Term("新", Array("a", "an", "b", "d", "j", "ng", "nr", "v"))
      )
    )

  )

  test("cases") {
    val segment = new ForwardMMSegment(
      Array(
        termDictLoader.loadDict(dictPathRoot + "/core.dict"),
        termDictLoader.loadDict(dictPathRoot + "/ansj.dict")
      ),
      stopDictLoader.loadDict(dictPathRoot + "/stop.dict")
    )

    testcase.foreach { testcase =>

      val name = testcase._1
      val query = testcase._2
      val expected = testcase._3

      println(s"testing: ${name}")
      val terms = segment.parse(query).terms
      println(s"result:\n${terms.zipWithIndex.map { case (term, index) => s"${index}: ${term}" }.mkString("\n")}")
      assert(terms.length == expected.length)
      for (i <- 0 to expected.length - 1) {
        assert(terms(i) == expected(i))
      }
      println(s"test ${name} [passed]")

    }
  }

}
