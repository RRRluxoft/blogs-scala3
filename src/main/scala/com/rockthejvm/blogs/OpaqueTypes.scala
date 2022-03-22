package com.rockthejvm.blogs

object OpaqueTypes {

  import com.rockthejvm.blogs.OpaqueTypes.Graphics.Color

  //  case class Name(value: String) {
//    // logic:
//  }

  object SocialNetwork { // domain
    opaque type Name = String

    // 1 - companion
    object Name {
      def fromString(s: String): Option[Name] =
        if s.isEmpty || s.charAt(0).isLower then None else Some(s)
    }

    // 2 -extension method
    extension (n: Name) {
      def length: Int = n.length
    }

  }

  object Graphics {
    opaque type Color = Int // in hex
    opaque type ColorFilter <: Color = Int

    val Red: Color = 0xFF000000
    val Green: Color = 0x00FF0000
    val Blue: Color = 0x0000FF00
    val halfTransparency: ColorFilter = 0x88 // 50% transparency
  }

  import Graphics._
  case class OverlayFilter(c: Color)

  @main def opaqueTypes() = {
    println(
      s"""
         |Hello
         |""".stripMargin)

    import SocialNetwork._
    val name = (Name.fromString("Tom"))
    name.foreach(println)
    lazy val nameLength = name.map(_.length)
    nameLength.foreach(println)
    nameLength.foreach(println)

  }


}
