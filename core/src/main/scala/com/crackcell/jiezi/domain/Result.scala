package com.crackcell.jiezi.domain

/**
  * 分词结果
  *
  * @author Menglong TAN
  */
class Result(terms: Array[Term] = Array()) extends Serializable {
  def getTerms() = terms
}
