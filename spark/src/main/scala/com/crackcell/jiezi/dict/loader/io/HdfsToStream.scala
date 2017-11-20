package com.crackcell.jiezi.dict.loader.io

import java.io.{ByteArrayInputStream, InputStream}

import org.apache.spark.sql.SparkSession

/**
  * 将HDFS路径转换成流
  *
  * @author Menglong TAN
  */
class HdfsToStream extends ToStream[String] {

  private lazy val spark = SparkSession.builder().getOrCreate()

  override def toStream(path: String): InputStream = {
    val data = spark.read.textFile(path).collect().filter(_.nonEmpty).mkString("\n")
    new ByteArrayInputStream(data.getBytes())
  }
}
