package service

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.google.inject.Inject
import com.google.inject.Singleton

import model.User
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

  def buyBullets(id: Long, amount: Int): Future[String] = {
    val usr = usersRepository.get(id)
    if (amount < 0)
      Future("You can not buy a negative amount of bullets")
    else {
      usr flatMap {
        case Some(user) => usersRepository.buyBullets(user, amount) map (_ => "Success! You bought " + amount + " bullets")
        case None       => Future("User not found")
      }
    }
  }

  def doCrime(id: Long): Future[String] = {
    usersRepository.get(id).flatMap {
      case Some(user) => crimeService.doAction(user)
      case None       => Future("Invalid user")
    }
  }

  def listAllUsers: Future[Seq[User]] = {
    usersRepository.listAll
  }
}
