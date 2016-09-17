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
  def whenNextAction(user: User): Future[Int] =
    waitingTimeService.getWaitingTimeByUser(user, whenNextActionAux _)

  def whenNextActionAux(elem: WaitingTime): Future[Int]

  def doAction(user: User): Future[String] //perfom action logic

  def refresh: (WaitingTime, Timestamp) => Future[Int]

  def setHot(user: User): Future[Int] = {
    waitingTimeService.getWaitingTimeByUser(user,
      { elem: WaitingTime => refresh(elem, calculateNextActionTime) })
  }

  //return timestamp of next action
  def calculateNextActionTime: Timestamp = {
    val d = new Date
    d.setTime(d.getTime + actionTime)
    new Timestamp(d.getTime)
  }
}