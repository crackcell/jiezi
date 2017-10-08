package com.crackcell.jiezi.util

import org.scalatest.FunSuite

/**
  * 测试用例
  *
  * @author Menglong TAN
  */
class RadixTreeTest extends FunSuite {

  test("put") {
    val tree = new RadixTree[Int]
    tree.put("word", 1)
    tree.put("world", 1)
    tree.put("wonder", 1)
    tree.findPrefixWords("wor").foreach { case (word, value) =>
      println(s"${word} -> ${value}")
    }
  }

}
