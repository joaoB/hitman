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
    waitingTimeService.getWaitingTimeByUser(user, {
      wt =>
        {
          val time = whenNextAction(wt)
          if (time < 0) {
            val prize = crimeAmount
            usersRepository.addMoney(user, prize)
            setHot(wt)
            "You did " + prize
          } else {
            "You still have to wait " + (time / 1000).toInt + " seconds!"
          }
        }
    })
  }

  def refresh = {
    (elem: WaitingTime, t: Timestamp) => waitingTimeService.refreshCrime(elem, t)
  }

  def whenNextAction(elem: WaitingTime): Long = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    elem.crime.getTime - now.getTime
  }
}