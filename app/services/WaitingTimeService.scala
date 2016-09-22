package service

import java.sql.Timestamp
import java.util.Calendar
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

import model.WaitingTime
import model.WaitingTimes
import com.google.inject._
import model.User

class WaitingTimeService @Inject() (waitingTimeRepository: WaitingTimes) {

  def create(userId: Long): Future[Boolean] = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    val wt = WaitingTime(0, now, now, userId)
    waitingTimeRepository.add(wt)
  }

  def getWaitingTimeByUser(user: User, f: WaitingTime => String): Future[String] = {
    waitingTimeRepository.getByUser(user).map {
      case Some(wt) => f(wt)
      case None     => "Something went very very wrong"
    }
  }

  def resetTimesByUser(user: User): Future[String] =
    getWaitingTimeByUser(user, 
      resetTimes(_)
    )

  def resetTimes(elem: WaitingTime): String = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    waitingTimeRepository.resetTimes(elem, now)
    "Times reseted for user " + elem.user
  }

  def refreshCrime(waitingTime: WaitingTime, timestamp: Timestamp): Future[Int] =
    waitingTimeRepository.refreshCrime(waitingTime, timestamp)

  def refreshBullets(waitingTime: WaitingTime, timestamp: Timestamp): Future[Int] =
    waitingTimeRepository.refreshBullets(waitingTime, timestamp)
}
