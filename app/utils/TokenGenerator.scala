package utils

/**
  * Created by zundra on 4/15/16.
  */

/*
  Token generator singleton object

  This object is the work horse responsible for encoding and decoding the short URL token values
 */

object TokenGenerator {
  // Shuffled array of valid characters that can appear in any given URL
  val ALPHA = List("F", "5", "m", "s", "1", "q", "d", "W", "O", "z", "e", "Y", "E", "N", "H", "L", "y", "Q", "4", "u", "X", "A", "t", "k", "R", "x", "6", "i", "n", "B", "p", "9", "v", "T", "o", "w", "C", "h", "K", "3", "V", "f", "g", "7", "I", "c", "2", "M", "U", "G", "J", "8", "S", "l", "a", "Z", "D", "r", "0", "j", "b", "P")

  // Convenience method for anything that requires to know the length of the ALPHA array
  val ALPHA_LEN = ALPHA.length

  // Convert a given index to its equivalent alphanumeric token
  def encode(idx: Long): String = {
    _convertToAlpha(_mapIndexToValue(idx)).mkString("")
  }

  // Convert a given token to its equivalent index value
  def decode(token: String): Int = {
    _decode(token.split(""))
  }

  /*
    Convert an array of strings -> integer
    For example ["F", "5", "m"] -> 012 since
    ALPHA[0] == F
    ALPHA[1] == 5
    ALPHA[2] == m
  */
  private def _decode(tokenList: Array[String], accumilator: Int = 0): Int = {
    if (tokenList.length == 0) return accumilator

    val character = tokenList.head
    val idx = accumilator * ALPHA_LEN + ALPHA.indexOf(character)
    val listEnd = tokenList.tail

    _decode(listEnd, idx)
  }

  // Recursively map a given Long value to its associated sequence of strings and return this value
  // in the form of a List of strings
  private def _mapIndexToValue(num: Long, accumilator: List[Int] = List()): List[Int] = {
    if (num == 0) {
      return accumilator.reverse
    }

    val n: Int = (num % ALPHA_LEN).toInt
    val acc = accumilator :+ n
    val newNum = num / ALPHA_LEN

    _mapIndexToValue(newNum, acc)
  }

  // Convert a lost of integer values to its ALPHA array equivalent
  private def _convertToAlpha(digits: List[Int]): List[String] = {
    digits.map(d => ALPHA(d))
  }
}
