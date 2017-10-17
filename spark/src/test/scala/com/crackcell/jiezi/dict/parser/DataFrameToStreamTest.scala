package com.crackcell.jiezi.dict.parser

import com.crackcell.jiezi.segment.ForwardMMSegment
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import org.scalatest.FunSuite

/**
  * 测试用例
  *
  * @author Menglong TAN
  */
class DataFrameToStreamTest extends FunSuite {
  lazy val spark = SparkSession.builder().master("local[*]")
      .enableHiveSupport().config("hive.exec.dynamic.partition.mode", "nonstrict")
      .getOrCreate()
  spark.sparkContext.setLogLevel("warn")

  // 准备数据
  val dict = spark.createDataFrame(
    spark.sparkContext.parallelize(Seq(
      Row("连衣裙", Array("infoword", "prop"), 10000L, "default", "v1"),
      Row("充电宝", Array("infoword"), 10000L, "default", "v1")
    )),
    StructType(Seq(
      StructField("keyword", StringType, nullable = false),
      StructField("nature_list", ArrayType(StringType, false), nullable = false),
      StructField("frequency", LongType, nullable = false),
      StructField("name", StringType, nullable = false),
      StructField("version", StringType, nullable = false)
    ))
  )

  val loader = new TermDictLoader(new DataFrameToStream)
  val coreDict = loader.loadDict(dict)
  val segment = new ForwardMMSegment(coreDict)

  test("Wordseg with default dict") {
    segment.setHandleInvalid("skip").parse("17年全新时尚连衣裙").terms.foreach(println)
  }

}
