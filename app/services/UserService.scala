package service

import model.{ User, Users }
import scala.util.{ Failure, Success }
import scala.concurrent.Future
import services.CrimeService

object UserService {

  def addUser(user: User): Future[Boolean] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    Users.add(user).map(
      WaitingTimeService.create(_))
      .recover { case _ => Future(false) }.flatMap(x => x)
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
    import scala.concurrent.ExecutionContext.Implicits.global
    val usr = Users.get(id)
    usr map {
      case Some(user) => {
        val cp = CrimeService.canPerform(user)
        cp.map {
          case time if time < 0 => {
            val prize = CrimeService.crimeAmount
            Users.addMoney(user, prize)
            CrimeService.refresh(user)
          }
          case other => {
            println("brrrrrrr" + other)
          }
        }.recover { case _ => 0 }

      }
      case None => 0
    }

  }

  def listAllUsers: Future[Seq[User]] = {
    Users.listAll
  }
}
