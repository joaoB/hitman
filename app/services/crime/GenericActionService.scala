package services.crime

import scala.concurrent.Future
import model.User
import java.sql.Timestamp
import java.util.Date
import model.WaitingTimes
import scala.concurrent.ExecutionContext.Implicits.global
import model.WaitingTime

abstract class GenericActionService(waitingTimeRepository: WaitingTimes) {
  val actionTime: Int // how long user wait between actions

  // how long user has to wait for next action
  def whenNextAction(user: User): Future[Long] = {
    waitingTimeRepository.getByUser(user) map {
      case Some(elem) => whenNextActionAux(elem)
      case None       => actionTime
    }
  }

  def whenNextActionAux(elem: WaitingTime): Long

  def doAction(user: User): Future[String] //perfom action logic

  def setHot(user: User): Future[Int]
  //return timestamp of next action
  def calculateNextActionTime: Timestamp = {
    val d = new Date
    d.setTime(d.getTime + actionTime)
    new Timestamp(d.getTime)
  }
}