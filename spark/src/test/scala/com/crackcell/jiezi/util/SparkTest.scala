package com.crackcell.jiezi.util

import org.apache.spark.SparkContext
import org.apache.spark.sql.{SQLContext, SQLImplicits, SparkSession}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Suite}

/**
  * 测试用例Spark上下文
  *
  * @author Menglong TAN
  */
trait SparkTest extends FunSuite with BeforeAndAfterAll {
  self: Suite =>

  @transient var spark: SparkSession = _
  @transient var sc: SparkContext = _

  override def beforeAll(): Unit = {
    super.beforeAll()
    spark = SparkSession.builder
      .master("local[2]")
      .appName("MLlibUnitTest")
      .getOrCreate()
    sc = spark.sparkContext
    spark.sparkContext.setLogLevel("warn")
  }

  override def afterAll(): Unit = {
    if (spark != null) {
      spark.stop()
    }
    super.afterAll()
  }

  protected object testImplicits extends SQLImplicits {
    protected override def _sqlContext: SQLContext = spark.sqlContext
  }

}
