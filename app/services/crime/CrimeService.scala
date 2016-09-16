package services.crime

import model.User
import model.WaitingTimes
import scala.concurrent.ExecutionContext.Implicits.global
import java.sql.Timestamp
import java.util.Date;
import scala.concurrent.Future
import java.util.Calendar
import model.WaitingTime

class CrimeService extends CrimeServiceBase {

  val crimeTime = 60 * 1000

  def crimeAmount : Int = {
    Math.random * 1000 toInt
  }

  def canPerform(user: User): Future[Long] = {
    WaitingTimes.getByUser(user) map {
      case Some(elem) => canPerformAux(elem)
      case None => crimeTime
    }
  }

  private def canPerformAux(elem: WaitingTime) = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    val compareResult = now.compareTo(elem.crime)
    if (compareResult > 0) elem.crime.getTime - now.getTime else 0
  }

  def refresh(user: User) = {
    WaitingTimes.getByUser(user) map {
      case Some(elem) =>
        WaitingTimes.refreshCrime(user, calculateNextCrimeTime)
      case None =>
    }
  }

  private def calculateNextCrimeTime = {
    val d = new Date
    d.setTime(d.getTime + crimeTime)
    new Timestamp(d.getTime)
  }

}