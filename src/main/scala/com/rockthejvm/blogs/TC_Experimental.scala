package com.rockthejvm.blogs

object TC_Experimental extends App {

  case class User(name: String, age: Int)

  // V_1
  // 1. type class itselsf
  trait HTMlSerializer[T] { self =>
    def serialize(value: T): String
  }

  // 2. instances
/* object HTML {
    implicit object HTMlSerializer extends HTMlSerializer[User] {
      override def serialize(value: User): String = s"<div>${value.name} is ${value.age} years old</div>"
    }
  }
*/
  // 2. instances - better :)
  given userSerializer: HTMlSerializer[User] with {
    override def serialize(value: User): String = {
      val User(name, age) = value
      s"<div>${value.name} is ${value.age} years old</div>"
    }
  }
  
  private given intSerializer: HTMlSerializer[Int] with {
    override def serialize(value: Int): String = s"<div>Int value is: $value</div>"
  }

  val user1 = User("First User", 34)
  println(userSerializer.serialize(user1))


  // V_2
  object HTMlSerializerOps {
    extension[T] (value: T)
      def toHTML(using serializer: HTMlSerializer[T]): String = serializer.serialize(value)
  }

  import HTMlSerializerOps.*
  println(user1.toHTML)
}
