package services.crime

import java.sql.Timestamp
import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.google.inject._

import model.User
import model.Users
import model.WaitingTimes
import java.util.Calendar
import model.WaitingTime

class CrimeService @Inject() (usersRepository: Users, waitingTimeRepository: WaitingTimes) extends GenericActionService {
  val actionTime: Int = 60 * 1000

  private def crimeAmount: Int = {
    Math.random * 1000 toInt
  }

  def doAction(user: User): Future[String] = {
    def doCrimeAux(user: User, time: Long): Future[String] = {
      if (time < 0) {
        val prize = crimeAmount
        usersRepository.addMoney(user, prize)
        setHot(user)
        Future("You did " + prize)
      } else {
        Future("You still have to wait " + (time / 1000).toInt + " seconds!")
      }
    }
    for {
      time <- whenNextAction(user)
      result <- doCrimeAux(user, time)
    } yield result
  }

  def setHot(user: User): Future[Int] = {
    waitingTimeRepository.getByUser(user) flatMap {
      case Some(elem) =>
        waitingTimeRepository.refreshCrime(user, super.calculateNextActionTime)
      case None => Future(-1) //something went bad
    }
  }

  def whenNextAction(user: User): Future[Long] = {
    waitingTimeRepository.getByUser(user) map {
      case Some(elem) => whenNextActionAux(elem)
      case None       => actionTime
    }
  }

  private def whenNextActionAux(elem: WaitingTime) = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    elem.crime.getTime - now.getTime
  }
}