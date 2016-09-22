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

@Singleton
class CrimeService @Inject() (
    usersRepository: Users,
    waitingTimeService: WaitingTimeService) extends GenericActionService(waitingTimeService) {

  val actionTime: Int = 60 * 1000

  private def crimeAmount: Int = {
    //Math.random * 1000 toInt
    1
  }

  def doAction(user: User): Future[String] = {
    waitingTimeService.getWaitingTimeByUser(user, {
      wt =>
        {
          whenNextAction(wt).fold(
            nextActionMessage => nextActionMessage,
            result => {
              val prize = crimeAmount
              usersRepository.addMoney(user, prize)
              refresh(wt, super.calculateNextActionTime)
              "You did " + prize
            })
        }
    })
  }

  def refresh(elem: WaitingTime, t: Timestamp) =
    waitingTimeService.refreshCrime(elem, t)

  def getTimeOfNextAction(elem: WaitingTime) = elem.crime.getTime

}