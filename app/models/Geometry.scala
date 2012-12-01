package models


case class Cord(x : Double, y : Double)

trait Geometry {
  def points() : List[Cord]
}


class Point(val cord : Cord) extends Geometry {
  def points() = List(cord)
}

class PloyLines(val cords : List[Cord]) extends Geometry {
  def points() = cords
}

// while this looks the same as the class above this needs to be rendered slightly differently
// so we need a different class so that we can build this correctly.
class Polygon(val cords : List[Cord]) extends Geometry {
  def points() = cords
}