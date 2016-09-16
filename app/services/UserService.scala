package service

import scala.concurrent.Future

import model.User
import services.crime.CrimeService
import services.crime.CrimeServiceBase
import scala.concurrent.ExecutionContext.Implicits.global
import model.Users
import com.google.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import services.crime.CrimeService
import model.Users
import services.crime.CrimeService

class UserService @Inject() (usersRepository: Users, crimeService: CrimeService, waitingTimeService: WaitingTimeService) {
  
  def addUser(user: User): Future[Boolean] = {
    usersRepository.add(user).flatMap(waitingTimeService.create(_)).recover { case _ => false }
  }

  def deleteUser(id: Long): Future[Int] = {
    usersRepository.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    usersRepository.get(id)
  }

  def buyBullets(id: Long, amount: Int) = {
    val usr = usersRepository.get(id)
    usr map {
      case Some(user) => usersRepository.buyBullets(user, amount)
      case None       => 0
    }
  }

  def doCrime(id: Long): Future[String] = {
    usersRepository.get(id).flatMap {
      case Some(user) => crimeService.doCrime(user)
      case None       => Future("Invalid user")
    }
  }

  def listAllUsers: Future[Seq[User]] = {
    usersRepository.listAll
  }
}
