package com.crackcell.jiezi.util

/**
  * 字符帮助函数
  *
  * @author Menglong TAN
  */
object StringUtils extends Serializable {

  val operators = Array('+', '-')

  val delimiters = Array(' ', ',', '.', '，', '。')

  def isEnglish(ch: Char): Boolean = ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z'

  def isDigit(ch: Char): Boolean = ch >= '0' && ch <= '9'

  def isNumber(ch: Char): Boolean = ch >= '0' && ch <= '9' || ch == '.' || isOperator(ch)

  def isOperator(ch: Char): Boolean = operators.contains(ch)

  def isDelimiter(ch: Char): Boolean = delimiters.contains(ch)

}
