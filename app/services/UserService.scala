package service

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.google.inject.Inject
import com.google.inject.Singleton

import model.User
import model.Users
import services.crime.CrimeService
import services.crime.BulletsService

class UserService @Inject() (
    usersRepository: Users,
    crimeService: CrimeService,
    bulletsService: BulletsService,
    waitingTimeService: WaitingTimeService) {

  def addUser(user: User): Future[Boolean] = {
    usersRepository.add(user).flatMap(waitingTimeService.create(_)).recover { case _ => false }
  }

  def deleteUser(id: Long): Future[Int] = {
    usersRepository.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    usersRepository.get(id)
  }

  def buyBullets(id: Long, amount: Int): Future[String] =
    usersRepository.get(id).flatMap {
      case Some(user) => bulletsService.doAction(user, amount)
      case None       => Future("Invalid user")
    }

  def doCrime(id: Long): Future[String] =
    usersRepository.get(id).flatMap {
      case Some(user) => crimeService.doAction(user)
      case None       => Future("Invalid user")
    }

  def listAllUsers: Future[Seq[User]] = {
    usersRepository.listAll
  }
}
