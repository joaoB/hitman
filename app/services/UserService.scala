package service

import model.{ User, Users }
import scala.util.{ Failure, Success }
import scala.concurrent.Future
import services.CrimeService

object UserService {

  def addUser(user: User) = {
    import scala.concurrent.ExecutionContext.Implicits.global
    Users.add(user).map {
      id => WaitingTimeService.create(id)
    }.recover{case _ => Future(false)}
  }

  def deleteUser(id: Long): Future[Int] = {
    Users.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    Users.get(id)
  }

  def buyBullets(id: Long, amount: Int) = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val usr = Users.get(id)
    usr map {
      case Some(user) => Users.buyBullets(user, amount)
      case None       => 0
    }
  }

  def doCrime(id: Long) = {
    val prize = CrimeService.crimeAmount
    import scala.concurrent.ExecutionContext.Implicits.global
    val usr = Users.get(id)
    usr map {
      case Some(user) => Users.addMoney(user, prize)
      case None       => 0
    }
  }

  def listAllUsers: Future[Seq[User]] = {
    Users.listAll
  }
}
