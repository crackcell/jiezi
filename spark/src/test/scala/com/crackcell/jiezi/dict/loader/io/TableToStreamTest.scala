package com.crackcell.jiezi.dict.loader.io

import com.crackcell.jiezi.dict.loader.TermDictLoader
import com.crackcell.jiezi.segment.ForwardMMSegment
import com.crackcell.jiezi.util.SparkTest
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.scalatest.FunSuite

/**
  * 测试用例
  *
  * @author Menglong TAN
  */
class TableToStreamTest extends SparkTest {

  import testImplicits._

  private lazy val infoDict = Seq(
    ("连衣裙", Array("infoword", "prop"), 10000L, "default", "v1"),
    ("充电宝", Array("infoword"), 10000L, "default", "v1")
  ).toDF("keyword", "nature_list", "frequency", "name", "version")

  private lazy val coreDict = Seq(
    ("1", Array("m"), 10000L, "default", "v1"),
    ("7", Array("m"), 10000L, "default", "v1")
  ).toDF("keyword", "nature_list", "frequency", "name", "version")

  test("Wordseg with default dict") {
    infoDict.createOrReplaceTempView("default")
    val loader = new TermDictLoader(new TableToStream)
    val segment = new ForwardMMSegment(Array(
      loader.loadDict("default")
    ))
    segment.setHandleInvalid("skip").parse("17年全新时尚连衣裙").terms.foreach(println)
  }

}