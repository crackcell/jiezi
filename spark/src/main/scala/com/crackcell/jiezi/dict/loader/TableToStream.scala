package com.crackcell.jiezi.dict.loader

import com.crackcell.jiezi.WordsegException
import org.apache.commons.logging.LogFactory
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}

/**
  * 将Hive表路径转换成流
  *
  * @author Menglong TAN
  */
class TableToStream extends SQLToStream {

  private val logger = LogFactory.getLog(classOf[TableToStream])

  private lazy val spark = SparkSession.builder().getOrCreate()

  override def toStream(path: String) = {
    val tokens = path.split("/")
    val table = tokens(0)
    val condSize = tokens.length - 1
    val conditions = if (tokens.length > 1) {
      tokens.drop(1).mkString(" AND ")
    } else {
      ""
    }

    super.toStream(s"SELECT * FROM ${table} WHERE ${conditions}")
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
        case ArrayType(StringType, _) => row.getSeq[String](index).mkString(",")
        case _ => throw new WordsegException(s"invalid type: ${f.name}, ${f.dataType}")
      }
    }.mkString("\t")
  }
}