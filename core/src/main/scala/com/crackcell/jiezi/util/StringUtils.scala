package com.crackcell.jiezi.util

/**
  * 字符帮助函数
  *
  * @author Menglong TAN
  */
object StringUtils {

  def isEnglish(ch: Char): Boolean = ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z'

  def isEnglishWord(ch: Char): Boolean = ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch == '-'

  def isDigit(ch: Char): Boolean = ch >= '0' && ch <= '9'

  def isNumber(ch: Char): Boolean = ch >= '0' && ch <= '9' || ch == '.'

}
