package models

import models.Tag._


object Tag {
  sealed abstract class Tag
  case object CC extends Tag
  case object CD extends Tag
  case object CJ extends Tag
  case object DT extends Tag
  case object EX extends Tag
  case object FW extends Tag
  case object IN extends Tag
}

case class Constraint(
  words: Set[String]  = Set.empty[String],
  tags: Set[Tag]      = Set.empty[Tag]
) {
  def apply(tag: Tag) = new Constraint(tags = Set(tag))
  def unapply(c: Constraint): Tag = c.tags
}

object Constraint {

}

case class Pattern(sequence: Seq[Constraint]) {
  def m() = sequence match {
    case Constraint(c) :: CD :: CC :: _ =>
      println("match")
  }
}
