package com.rockthejvm.blogs

object Traits {

  trait Talker(subject: String) {
    def talkTo(another: Talker): String = ""
  }

  class Person(name: String) extends Talker("misic") {
    override def talkTo(another: Talker): String = " "
  }

  class RockFan extends Talker("rock")
  class RockFanatic extends RockFan with Talker // ("heavy metal")  <- no constructor arg !!!

  // derived traits
  trait BrokenRecord extends Talker
  class AnnoyingFriend extends BrokenRecord with Talker("politics")

  // super trait
  trait Paintable // 'super trait' doesnt work anymore 
  trait Color
  case object Red extends Color // with Paintable
  case object Green extends Color // with Paintable
  case object Blue extends Color // with Paintable

  val color: Color = if 43 > 2 then Red else Blue

  def main(args: Array[String]): Unit = {}
}
