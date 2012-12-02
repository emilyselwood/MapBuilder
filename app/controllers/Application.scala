package controllers

import play.api._
import play.api.mvc._
import models.PointCollection
import util.control.NonFatal

object Application extends Controller {

  val defaultMap = PointCollection(0, "Default PointCollection", 0f, 0f, 3, "Sat")

  val pointCollections = PointCollection.getAll()

  val working = if(pointCollections.isEmpty) {
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




}