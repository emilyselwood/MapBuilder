package controllers

import play.api._
import play.api.mvc._
import models.PointCollection

object Application extends Controller {

  val defaultMap = PointCollection(0, "Default PointCollection", 0f, 0f, 3, "Sat")

  val pointCollections = PointCollection.getAll()


  def index = Action {
    Ok(views.html.index(defaultMap))
  }



}