package models

import play.api.db.DB
import anorm._
import play.api.Play.current
import java.math.BigDecimal


case class PointCollection(var id : Long, name : String,
          centerPointX : Double, centerPointY : Double,
           zoomLevel : Int,
           mapType : String,
           points : List[Geometry] = List()) {

}

object PointCollection {

  def unapply(value : String) : Option[PointCollection] = {
    val Regex = "PointCollection\\(([^,]*),([^,]*),([^,]*),([^,]*),([^,]*),([^,]*),[^)]*\\)".r

    value match {
      case Regex(id, name, centerX, centerY, zoomLevel, mapType, points) => Some(PointCollection(id.toInt, name, centerX.toDouble, centerY.toDouble, zoomLevel.toInt, mapType, List()))
      case _ => None
    }
  }

  def getAll() : List[PointCollection] = {

    DB.withConnection { implicit c =>

      val query = SQL(
        """SELECT id,
          |   name,
          |   description,
          |   "centerX",
          |   "centerY",
          |   "zoomLevel",
          |   "defaultStyle"
          |FROM "PointCollections";
        """.stripMargin
      )

      try {
        query().map ( row =>
          PointCollection(
            row[Long]("id"),
            row[String]("name"),
            row[BigDecimal]("centerX").doubleValue(),
            row[BigDecimal]("centerY").doubleValue(),
            row[Int]("zoomLevel"),
            row[String]("defaultStyle")
          )
        ).toList
      }
      catch {
        case e : Exception =>
          println("there was an exception" + e.getMessage)
          List()
      }
    }
  }

  def update(point : PointCollection) {
    DB.withConnection { implicit c =>


      val query = SQL(
        """
          |update "PointCollections"
          |set "name" = {mapName},
          |    description = null,
          |    "centerX" = {centerX},
          |    "centerY" = {centerY},
          |    "zoomLevel" = {zoomLevel},
          |    "defaultStyle" = {defaultStyle}
          |WHERE id = {id}
        """.stripMargin).on(
        ("mapName"      -> point.name),
        ("centerX"      -> point.centerPointX),
        ("centerY"      -> point.centerPointY),
        ("zoomLevel"    -> point.zoomLevel),
        ("defaultStyle" -> point.mapType),
        ("id"           -> point.id)
      )


      query.executeUpdate()
    }
  }

  def insert(point : PointCollection) : PointCollection = {

    DB.withConnection { implicit c =>
      val query = SQL(
        """
          |insert into "PointCollections"(
          |   id,
          |   name,
          |   "description",
          |   "centerX",
          |   "centerY",
          |   "zoomLevel",
          |   "defaultStyle")
          |values( {name}, {centerX}, {centerY}, {zoomLevel}, {defaultStyle} );
        """.stripMargin)

      query.on(("name"  -> point.name),
        ("centerX"      -> point.centerPointX),
        ("centerY"      -> point.centerPointY),
        ("zoomLevel"    -> point.zoomLevel),
        ("defaultStyle" -> point.mapType)
      )

      point.id = query.executeInsert().getOrElse(-1l)

      point
    }
  }

}
