package services.crime

import java.sql.Timestamp
import java.util.Date

import scala.concurrent.Future

import model.User
import model.WaitingTime
import service.WaitingTimeService
import java.util.Calendar

abstract class GenericActionService(waitingTimeService: WaitingTimeService) {
  val actionTime: Int // how long user wait between actions

  // how long user has to wait for next action
  def whenNextAction(elem: WaitingTime): Either[String, Boolean] = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    val nextAction = getTimeOfNextAction(elem) - now.getTime
    if (nextAction <= 0)
      Right(true)
    else
      Left("You still have to wait " + (nextAction / 1000).toInt + " seconds!")
  }

  def getTimeOfNextAction(elem: WaitingTime): Long

  def doAction(user: User): Future[String] //perfom action logic

  def refresh(elem: WaitingTime, t: Timestamp): Future[Int]

  //return timestamp of next action
  def calculateNextActionTime: Timestamp = {
    val d = new Date
    d.setTime(d.getTime + actionTime)
    new Timestamp(d.getTime)
  }
}