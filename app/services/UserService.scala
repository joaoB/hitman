package service

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.google.inject.Inject
import com.google.inject.Singleton

import model.User
import model.Users
import services.crime.CrimeService
import services.crime.BulletsService
import service.WaitingTimeService

trait UserServices{
  def addUser(user: User) : Future[Boolean]
}

class UserService @Inject() (
    usersRepository: Users,
    crimeService: CrimeService,
    bulletsService: BulletsService,
    waitingTimeService: WaitingTimeService) extends UserServices{

  def addUser(user: User): Future[Boolean] = {
    usersRepository.add(user).flatMap(waitingTimeService.create(_)).recover { case _ => false }
  }

  def deleteUser(id: Long): Future[Int] = {
    usersRepository.delete(id)
  }

  def getUser(id: Long): Future[Option[User]] = {
    usersRepository.get(id)
  }

  def getUser(id: Long, f: User => Future[String]): Future[String] =
    getUser(id).flatMap {
      case Some(user) => f(user)
      case None       => Future("Invalid user")
    }

  def buyBullets(id: Long, amount: Int): Future[String] =
    getUser(id, bulletsService.doAction(_, amount))

  def doCrime(id: Long): Future[String] =
    getUser(id, crimeService.doAction)

  def listAllUsers: Future[Seq[User]] = {
    usersRepository.listAll
  }
}
