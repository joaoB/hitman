package service

import scala.concurrent.Future

import model.User
import model.Users
import services.crime.CrimeService
import services.crime.CrimeServiceBase
import scala.concurrent.ExecutionContext.Implicits.global

object UserService extends UserService(new CrimeService)

class UserService(crimeService: CrimeServiceBase) {

  def addUser(user: User): Future[Boolean] = {
    Users.add(user).flatMap(WaitingTimeService.create(_)).recover { case _ => false }
  }

  def deleteUser(id: Long): Future[Int] = {
    Users.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    Users.get(id)
  }

  def buyBullets(id: Long, amount: Int) = {
    val usr = Users.get(id)
    usr map {
      case Some(user) => Users.buyBullets(user, amount)
      case None       => 0
    }
  }

  def doCrime(id: Long): Future[String] = {
    import scala.concurrent.ExecutionContext.Implicits.global

    Users.get(id).flatMap {
      case Some(user) => crimeService.doCrime(user)
      case None       => Future("Invalid user")
    }
  }

  def listAllUsers: Future[Seq[User]] = {
    Users.listAll
  }
}
