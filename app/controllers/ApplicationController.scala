package controllers

import model.User
import play.api.mvc._
import scala.concurrent.Future
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationController extends Controller {

  def index = Action.async { implicit request =>
    UserService.listAllUsers map { users =>
      Ok(views.html.index(users))
    }
  }

  def buyBullets(id: Long, amount: Int) = Action.async { implicit request =>
    UserService.buyBullets(id, amount) map { result =>
      Ok("Ok")
    }
  }

  def doCrime(id: Long) = Action.async { implicit request =>
    UserService.doCrime(id) map { result =>
      Ok("Ok")
    }
  }

  def addUser() = Action.async { implicit request =>
    val newUser = User(0, "a", 0, 0, 1, 1)
    UserService.addUser(newUser).map(res =>
      Redirect(routes.ApplicationController.index()))
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    UserService.deleteUser(id) map { res =>
      Redirect(routes.ApplicationController.index())
    }
  }

}

