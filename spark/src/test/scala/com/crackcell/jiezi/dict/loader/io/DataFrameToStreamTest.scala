package com.crackcell.jiezi.dict.loader.io

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

  lazy val spark = SparkSession.builder().master("local[*]").enableHiveSupport().getOrCreate()

  spark.sparkContext.setLogLevel("warn")

  // 准备数据
  val infoDict = spark.createDataFrame(
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

  val coreDict = spark.createDataFrame(
    spark.sparkContext.parallelize(Seq(
      Row("1", Array("m"), 10000L, "default", "v1"),
      Row("7", Array("m"), 10000L, "default", "v1")
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
  val segment = new ForwardMMSegment(Array(
    loader.loadDict(infoDict),
    loader.loadDict(coreDict)
  ))

  test("Wordseg with default dict") {
    segment.parse("17连衣裙").terms.foreach(println)
  }

}
