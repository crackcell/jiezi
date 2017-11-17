package com.crackcell.jiezi.domain

import org.json4s.JsonDSL._

/**
  * 分词结果
  *
  * @author Menglong TAN
  */
case class Result(terms: Array[Term] = Array()) extends Serializable with Iterable[Term] with ToJson {
  override def iterator = terms.iterator

  override def jsonValue = ("terms" -> terms.map(_.jsonValue).toList)
}

object Result {
  def apply(query: String, terms: Array[Term]): Result =
    new Result(
      terms = terms.map { term =>
        new Term(term).setWord(query.substring(term.offset, term.offset + term.length))
      }
    )
}
