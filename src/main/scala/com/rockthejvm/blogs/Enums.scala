package com.rockthejvm.blogs

object Enums {

  enum Permissions {
    case READ, WRITE, EXEC, NONE
  }
  val read: Permissions = Permissions.READ

  enum PermissionsWithBits(val bits: Int) {
    case READ extends PermissionsWithBits(4) // 100
    case WRITE extends PermissionsWithBits(2) // 010
    case EXEC extends PermissionsWithBits(1) // 001
    case NONE extends PermissionsWithBits(0) // 000

    def toHex: String = Integer.toHexString(bits)
  }
  val read2: PermissionsWithBits = PermissionsWithBits.READ
  val bitString = read2.bits
  val hexString = read2.toHex

  def main(args: Array[String]): Unit = {}
}
