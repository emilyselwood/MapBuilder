package models

import play.api.db.DB
import anorm._
import play.api.Play.current


case class PointCollection(id : Int, name : String,
          centerPointX : Double, centerPointY : Double,
           zoomLevel : Int,
           mapType : String,
           points : List[Geometry] = List()) {

}

object PointCollection {

  def getAll() : List[PointCollection] = {
    DB.withConnection { implicit c =>
      val query = SQL(
        """SELECT id,
          |   name,
          |   description,
          |   \"centerX\",
          |   \"centerY\",
          |   \"zoomLevel\",
          |   \"defaultStyle\"
          |FROM \"PointCollections\";
        """.stripMargin
      )

      query().map ( row =>
        PointCollection(
          row[Int]("id"),
          row[String]("name"),
          row[Double]("centerX"),
          row[Double]("centerY"),
          row[Int]("zoomLevel"),
          row[String]("defaultStyle")
        )
      ).toList
      //List.empty[PointCollection]
    }
  }

}
