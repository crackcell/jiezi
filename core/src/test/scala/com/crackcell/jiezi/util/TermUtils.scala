package com.crackcell.jiezi.util

import com.crackcell.jiezi.domain.Term

object TermUtils {

  def assertTerms(terms: Array[Term], testTerms: Term*): Unit = {
    for (term <- terms) {
      if (!testTerms.contains(term)) {
        throw new RuntimeException(s"failed: ${term} is not in ${testTerms}")
      }
    }
  }

}
