package com.crackcell.jiezi.dict.loader.io.io

import java.io.{File, FileInputStream, InputStream}

import com.crackcell.jiezi.WordsegException

/**
  * 本地路径转流
  *
  * @author Menglong TAN
  */
class FileToStream extends ToStream[String] {
  override def toStream(path: String): InputStream = {
    val newPath =
      if (path.startsWith("file://"))
        path.substring(7)
      else
        path

    val file = new File(newPath)

    try new FileInputStream(file)
    catch {
      case e: Exception =>
        throw new WordsegException(
          s"path: ${path} file: ${file.getAbsolutePath} not found or is not readable exception: ${e}"
        )
    }
  }
}
