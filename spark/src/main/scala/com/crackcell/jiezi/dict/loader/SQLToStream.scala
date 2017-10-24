package com.crackcell.jiezi.dict.loader

import org.apache.spark.sql.SparkSession

/**
  * 将SQL语句执行结果转化成InputStream
  *
  * @author Menglong TAN
  */
class SQLToStream extends ToStream[String] {

  private lazy val spark = SparkSession.builder().getOrCreate()

  private lazy val dataFrameToStream = new DataFrameToStream

  override def toStream(sql: String) = {
    val df = spark.sql(sql)
    dataFrameToStream.toStream(df)
  }
}
