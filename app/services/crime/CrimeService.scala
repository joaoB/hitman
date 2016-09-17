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
import service.WaitingTimeService

class CrimeService @Inject() (
    usersRepository: Users,
    waitingTimeService: WaitingTimeService) extends GenericActionService(waitingTimeService) {
  
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

  def refresh = {
    (elem: WaitingTime, t: Timestamp) => waitingTimeService.refreshCrime(elem, t)
  }

  def whenNextActionAux(elem: WaitingTime): Future[Int] = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    Future(elem.crime.getTime - now.getTime toInt)
  }
}