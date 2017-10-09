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

  import RadixTree._

  val trie = new PatriciaTrie[E]()
  val status = new mutable.HashMap[Char, Int]()

  def isStartChar(ch: Char): Boolean = (getCharStatus(ch) & START_CHAR) != 0

  def isMiddleChar(ch: Char): Boolean = (getCharStatus(ch) & MIDDLE_CHAR) != 0

  def isEndChar(ch: Char): Boolean = (getCharStatus(ch) & END_CHAR) != 0

  def put(word: String, value: E): Unit =
    if (word != null && word.length != 0) {
      updateCharStatus(word.charAt(0), RadixTree.START_CHAR)
      updateCharStatus(word.last, RadixTree.END_CHAR)
      for (i <- 1 until word.length) updateCharStatus(word.charAt(i), RadixTree.MIDDLE_CHAR)
      trie.put(word, value)
    }

  def get(word: String): Option[E] = Option(trie.get(word))

  def findPrefixWords(prefix: String): mutable.Map[String, E] = trie.prefixMap(prefix)

  def findLongestHeadWord(query: String): Option[(String, E)] = {
    if (query == null || query.length == 0 || !isStartChar(query.charAt(0))) return None
    for (i <- query.length - 1 to 0 by -1) {
      if (isEndChar(query.charAt(i))) {
        val word = query.substring(0, i + 1)
        val value = trie.get(word)
        if (value != null) return Some((word, value))
      }
    }
    None
  }

  protected def getCharStatus(ch: Char): Int =
    if (!status.containsKey(ch))
      RadixTree.UNKNOWN_CHAR
    else
      status(ch)

  protected def updateCharStatus(ch: Char, charStatus: Int): Unit =
    status.put(
      ch,
      if (status.contains(ch))
        status(ch) | charStatus
      else
        charStatus
    )

}

object RadixTree {
  val UNKNOWN_CHAR = 0
  val START_CHAR = 1
  val MIDDLE_CHAR = 2
  val END_CHAR = 4
}