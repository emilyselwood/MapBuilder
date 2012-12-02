package controllers

import play.api._
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
      Ok(views.html.index(working, pointCollections))
    }
    catch {
      case NonFatal(e) => InternalServerError(e.getMessage)
    }
  }

  def staticmap = Action {
      try {
        Ok(views.html.staticmap(working, pointCollections))
      }
      catch {
        case NonFatal(e) => InternalServerError(e.getMessage)
      }
    }

  def savePointCollection = Action { request =>

    request.body.asJson.map { json =>
      println("saving a map *\\o/*")
      val id = (json \ "id").as[Long]
      val name = (json \ "name").as[String]
      //val description = (request.body \ "description").asOpt[String]

      val centerX = (json \ "centerX").as[Double]
      val centerY = (json \ "centerY").as[Double]

      val zoomLevel = (json \ "zoomLevel").as[Int]

      val mapType = (json \ "mapType").as[String]

      val newPoint = PointCollection(id, name, centerX, centerY, zoomLevel, mapType)

      PointCollection.update(newPoint)

      working = newPoint
      Ok
    }.getOrElse {
      BadRequest("that wasn't json data: " + request.body)
    }
  }




}