package com.crackcell.jiezi.domain

import org.json4s.JsonAST.JValue
import org.json4s.jackson.JsonMethods.{compact, pretty, render}

/**
  * 转换成Json值
  *
  * @author Menglong TAN
  */
trait ToJson {
  def jsonValue: JValue

  def json: String = compact(render(jsonValue))

  def prettyJson: String = pretty(render(jsonValue))
}
