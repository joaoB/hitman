package service

import java.sql.Timestamp
import java.util.Calendar

import scala.concurrent.Future

import model.WaitingTime
import model.WaitingTimes

object WaitingTimeService {

  def create(userId: Long): Future[Boolean] = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    val wt = WaitingTime(0, now, now, userId)
    WaitingTimes.add(wt)
  }
}
