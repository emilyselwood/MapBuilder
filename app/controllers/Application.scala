package controllers

import play.api._
import libs.json.{JsSuccess, JsResult, JsValue, Reads}
import play.api.mvc._
import models.PointCollection
import util.control.NonFatal

object Application extends Controller {

  val defaultMap = PointCollection(0, "Default PointCollection", 0f, 0f, 3, "Sat")

  val pointCollections = PointCollection.getAll()

  var working = if(pointCollections.isEmpty) {
    PointCollection.insert(defaultMap)
  }
  else {
    pointCollections.head
  }


  def index = Action {
    try {
      Ok(views.html.index(working))
    }
    catch {
      case NonFatal(e) => InternalServerError(e.getMessage)
    }
  }

  def savePointCollection = Action(parse.tolerantJson) { request =>

    val id = (request.body \ "id").as[Long]

    //val description = (request.body \ "description").asOpt[String]

    val centerX = (request.body \ "centerX").as[Double]
    val centerY = (request.body \ "centerY").as[Double]

    val zoomLevel = (request.body \ "zoomLevel").as[Int]

    val mapType = (request.body \ "mapType").as[String]
    val name = (request.body \ "mapName").as[String]
    val newPoint = PointCollection(id, name, centerX, centerY, zoomLevel, mapType)


    PointCollection.update(newPoint)
    println("saving a map *\\o/*")
    working = newPoint
    Ok

  }


  implicit def listOfList = Reads.list[List[Point]](Reads.list[Point](Point.PointReads))

  def savePoints = Action(parse.tolerantJson) { request =>
    println(request.body)

    //val mapId = (request.body \ "id").as[Long]

    //val points = (request.body \ "polyList" ).as[List[List[Point]]]

    //println("saving a selection of points *\\o/*")

    Ok
  }


}

object Point {
  implicit def PointReads = new Reads[Point] {

    def reads(js : JsValue) : JsResult[Point] = {
      JsSuccess(Point((js \ "lat").as[Double], (js \ "lng").as[Double]))
    }

  }
}
case class Point(lat : Double, lng : Double)
