package com.rockthejvm.blogs

object NewTypes {

  import java.io.File

  // 1 = literal types
  val three: 3 = 3 // 3 <: Int
  val scala: "Scala" = "Scala"

  def passNumber(n: Int) = println(n)
  passNumber(45)
  passNumber(three)

  // but:
  def passStrict(n: 3) = println(n)
  passStrict(three)
//  passStrict(43) // error

  val pi: 3.14 = 3.14
  val truth: true = true

  def doSome(meaning: Option[42]) = meaning.foreach(println)

  // 2 - union type
  def ambivalentMethod(arg: String | Int) = arg match {
    case _: String => println(s"a string $arg")
    case _: Int    => println(s"an int $arg")
  }

  type ErrorOr[T] = T | "error"

  def handleResource(file: ErrorOr[File]): Unit =
    // do smt
    ()

  val stringOrInt = if 43 > 0 then "a string" else 43
  val aStringOrInt: String | Int = if 43 > 0 then "a string" else 43

  // 3 - intersection types
  trait Camera {
    def takePhoto() = println("snap")
  }
  trait Phone {
    def makeCall() = println("ring")
  }

  def useSmartDevice(sp: Camera & Phone) = {
    sp.takePhoto()
    sp.makeCall()
  }

  class SmartPhone extends Camera, Phone
  useSmartDevice(new SmartPhone) // OK

  /**
   *
   */
  trait HostConfig
  trait HostController {
    def get: Option[HostConfig]
  }

  trait PortConfig
  trait PortController {
    def get: Option[PortConfig]
  }
  def getConfigs(controller: HostController & PortController): Option[HostConfig & PortConfig] = controller.get

  //  @main def newTypes(args: Array[String]): Unit =
//    println("New Types here")

  def main(args: Array[String]): Unit =
    println("New Types here")

}
