package com.crackcell.jiezi.dict.parser

import java.io.ByteArrayInputStream

import com.crackcell.jiezi.WordsegException
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
  * 将Hive表路径转换成流
  *
  * @author Menglong TAN
  */
class DataFrameToStream extends ToStream[DataFrame] {

  private val spark = SparkSession.builder().enableHiveSupport().getOrCreate()

  override def toStream(dataframe: DataFrame) = {
    val data = dataframe.collect().map(rowToLine).filter(_.size > 0).mkString("\n")
    new ByteArrayInputStream(data.getBytes())
  }

  private def rowToLine(row: Row): String = {
    val schema = row.schema
    schema.fields.filter(f => f.name != "name" && f.name != "version") .map { f =>
      val index = schema.fieldIndex(f.name)
      f.dataType match {
        case StringType => row.getAs[String](f.name)
        case IntegerType => row.getAs[Int](f.name).toString
        case LongType => row.getAs[Long](f.name).toString
        case FloatType => row.getAs[Float](f.name).toString
        case DoubleType => row.getAs[Double](f.name).toString
        case ArrayType(StringType, false) => row.getSeq[String](index).mkString(",")
        case _ => throw new WordsegException(s"invalid type: ${f.name}, ${f.dataType}")
      }
    }.mkString("\t")
  }
}
