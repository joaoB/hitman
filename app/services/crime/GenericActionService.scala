package services.crime

import java.sql.Timestamp
import java.util.Date

import scala.concurrent.Future

import model.User
import model.WaitingTime
import service.WaitingTimeService

abstract class GenericActionService(waitingTimeService: WaitingTimeService) {
  val actionTime: Int // how long user wait between actions

  // how long user has to wait for next action
  def whenNextAction(elem: WaitingTime): Long

  def doAction(user: User): Future[String] //perfom action logic

  def refresh: (WaitingTime, Timestamp) => Future[Int]

  def setHot(elem: WaitingTime): Future[Int] =
    refresh(elem, calculateNextActionTime)

  //return timestamp of next action
  def calculateNextActionTime: Timestamp = {
    val d = new Date
    d.setTime(d.getTime + actionTime)
    new Timestamp(d.getTime)
  }
}