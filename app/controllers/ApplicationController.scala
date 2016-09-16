package controllers

import model.User
import play.api.mvc._
import scala.concurrent.Future
import service.UserService
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ApplicationController @Inject()(userService: UserService) (implicit ec: ExecutionContext) extends Controller {

  def index = Action.async { implicit request =>
    userService.listAllUsers map { users =>
      Ok(views.html.index(users))
    }
  }

  def buyBullets(id: Long, amount: Int) = Action.async { implicit request =>
    userService.buyBullets(id, amount) map { result =>
      Ok(result)
    }
  }

  def doCrime(id: Long) = Action.async { implicit request =>
    userService.doCrime(id) map { result =>
      Ok(result)
    }
  }

  def addUser() = Action.async { implicit request =>
    val newUser = User(0, "a", 0, 0, 1, 1)
    userService.addUser(newUser).map(res =>
      Redirect(routes.ApplicationController.index()))
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    userService.deleteUser(id) map { res =>
      Redirect(routes.ApplicationController.index())
    }
  }

}

