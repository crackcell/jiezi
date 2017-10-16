package com.crackcell.jiezi.dict.parser

import org.apache.commons.logging.LogFactory
import org.apache.spark.sql.{Row, SparkSession}

/**
  * 将Hive表路径转换成流
  *
  * @author Menglong TAN
  */
class HiveTableToStream extends PathToStream {

  private val logger = LogFactory.getLog(classOf[HiveTableToStream])

  private val spark = SparkSession.builder().enableHiveSupport().getOrCreate()

  override def toStream(path: String) = {
    val tokens = path.split("/")
    val table = tokens(0)
    val condSize = tokens.length - 1
    val conditions = if (tokens.length > 1) {
      tokens.drop(1).mkString(" AND ")
    } else {
      ""
    }

    val df = spark.sql(s"SELECT * FROM ${table} WHERE ${conditions}")
    df.collect().map(rowToLine).filter(_.size > 0)
  }

  private def rowToLine(row: Row): String = {
    row.schema.fields.map { field =>




    }
  }
}
