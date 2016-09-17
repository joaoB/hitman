package services.crime

import scala.concurrent.Future
import model.User
import java.sql.Timestamp
import java.util.Date

abstract class GenericActionService {
  val actionTime: Int // how long user wait between actions
  def whenNextAction(user: User): Future[Long] // how long user has to wait for next action
  def doAction(user: User): Future[String] //perfom action logic

  def setHot(user: User): Future[Int]
  //return timestamp of next action
  def calculateNextActionTime: Timestamp = {
    val d = new Date
    d.setTime(d.getTime + actionTime)
    new Timestamp(d.getTime)
  }
}