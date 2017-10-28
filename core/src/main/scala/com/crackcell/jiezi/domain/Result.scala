package com.crackcell.jiezi.domain

/**
  * 分词结果
  *
  * @author Menglong TAN
  */
case class Result(terms: Array[Term] = Array()) extends Serializable with Iterable[Term] {
  override def iterator = terms.iterator
}

object Result {
  def apply(query: String, terms: Array[Term]): Result =
    new Result(
      terms = terms.map { term =>
        new Term(term).setWord(query.substring(term.offset, term.offset + term.length))
      }
    )
}
