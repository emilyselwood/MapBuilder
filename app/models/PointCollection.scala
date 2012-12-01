package models

import play.api.db.DB
import anorm._


case class PointCollection(id : Int, name : String,
          centerPointX : Float, centerPointY : Float,
           zoomLevel : Int,
           mapType : String) {



}

object PointCollection {
  def getAll() : List[PointCollection] = {
    /*DB.withConnection { implicit c =>
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

      query().map { row =>
        PointCollection(row[Int]("id"),
          row[String]("name"),
          row[Float]("centerX"),
          row[Float]("centerY"),
          row[Int]("zoomLevel"),
          row[String]("defaultStyle")
        ).toList
      }

      }*/
    List.empty[PointCollection]

  }
}
