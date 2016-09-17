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

  def getWaitingTimeByUser(user: User, f: WaitingTime => Future[Int]): Future[Int] =
    waitingTimeRepository.getByUser(user).flatMap {
      case Some(wt) => f(wt)
      case None     => Future(-1)
    }

  def refreshCrime(waitingTime: WaitingTime, timestamp: Timestamp): Future[Int] =
    waitingTimeRepository.refreshCrime(waitingTime, timestamp)

  def refreshBullets(waitingTime: WaitingTime, timestamp: Timestamp): Future[Int] =
    waitingTimeRepository.refreshBullets(waitingTime, timestamp)
}
