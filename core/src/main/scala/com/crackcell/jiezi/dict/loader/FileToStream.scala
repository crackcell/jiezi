package com.crackcell.jiezi.dict.loader

import java.io.{File, FileInputStream}

import com.crackcell.jiezi.WordsegException

/**
  * 本地路径转流
  *
  * @author Menglong TAN
  */
class FileToStream extends ToStream[String] {
  override def toStream(path: String) = {
    val newPath =
      if (path.startsWith("file://"))
        path.substring(7)
      else
        path

    val file = new File(path)

    try new FileInputStream(file)
    catch {
      case e: Exception =>
        throw new WordsegException(
          s"path: ${path} file: ${file.getAbsolutePath} not found or is not readable exception: ${e}"
        )
    }
  }
}
