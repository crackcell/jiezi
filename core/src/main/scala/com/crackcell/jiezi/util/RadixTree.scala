package com.crackcell.jiezi.util

import org.apache.commons.collections4.trie.PatriciaTrie

import scala.collection.mutable
import scala.collection.JavaConversions._

/**
  * 字典树
  *
  * @author Menglong TAN
  */
class RadixTree[E] {

  val trie = new PatriciaTrie[E]()
  val status = new mutable.HashMap[Char, Int]()

  def put(word: String, value: E): E = {
    if (word == null || word.length == 0) return value
    updateCharStatus(word.charAt(0), RadixTree.START_CHAR)
    updateCharStatus(word.last, RadixTree.END_CHAR)

    for (i <- 1 to word.length - 1) updateCharStatus(word.charAt(i), RadixTree.MIDDLE_CHAR)

    trie.put(word, value)
  }

  def get(word: String): E = trie.get(word)

  def findPrefixWords(prefix: String): mutable.Map[String, E] = trie.prefixMap(prefix)

  protected def updateCharStatus(ch: Char, charStatus: Int): Unit = {
    val newStatus =
      if (status.contains(ch))
        status.get(ch).get | charStatus
      else
        charStatus
    status.put(ch, newStatus)
  }

}

object RadixTree {
  val UNKNOWN_CHAR = 0
  val START_CHAR = 1
  val MIDDLE_CHAR = 2
  val END_CHAR = 4
}