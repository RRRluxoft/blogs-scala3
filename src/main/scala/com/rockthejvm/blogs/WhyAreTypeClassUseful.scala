package com.rockthejvm.blogs

object WhyAreTypeClassUseful extends App {


  trait Summable[T] {
    def sumUpElements(list: List[T]): T
  }

  implicit object IntSummable extends Summable[Int] {
    override def sumUpElements(list: List[Int]): Int = list.sum
  }
  implicit object StringSummable extends Summable[String] {
    override def sumUpElements(list: List[String]): String = list.mkString
  }
  def processList[T](list: List[T])(implicit summable: Summable[T]): T = {
    /**
     * summ up all elements of the list
     * for int => sum of the elements
     * for str => concat all elements
     *
     * for other types => ERROR
     */
    summable.sumUpElements(list)
  }

  println(processList(1:: 2:: 7 :: 3 :: Nil))
  println(processList(List("scala ", "cli ", "read")))

}
