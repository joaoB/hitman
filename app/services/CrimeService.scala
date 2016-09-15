package services

import model.User
import model.WaitingTimes
import scala.concurrent.ExecutionContext.Implicits.global
import java.sql.Timestamp
import java.util.Date;
import scala.concurrent.Future
import java.util.Calendar

object CrimeService {

  val crimeTime = 60 * 1000

  def crimeAmount = {
    Math.random * 1000 toInt
  }

  def canPerform(user: User): Future[Long] = {
    WaitingTimes.getByUser(user) map {
      case Some(elem) => {
        val now = new Timestamp(Calendar.getInstance.getTime.getTime)
        val compareResult = now.compareTo(elem.crime)
        println("next crime: " + elem.crime.toString)
        println("current time: " + now.toString)
        println("compareResult " + compareResult)
        if (compareResult > 0){
          val a = elem.crime.getTime - now.getTime
          println("next crime in " + a)
          a
        }
        else 0
        
      }
      case None => crimeTime
    }
  }

  def refresh(user: User) = {
    WaitingTimes.getByUser(user) map {
      case Some(elem) => {
        val calendar = Calendar.getInstance
        val now = new Timestamp(Calendar.getInstance.getTime.getTime)
        calendar.setTimeInMillis(now.getTime)
        calendar.add(Calendar.SECOND, crimeTime)
        val d = new Date
        d.setTime(d.getTime + crimeTime)
        val nextCrime = new Timestamp(d.getTime)
        println("next crime:::::::::::: " + nextCrime.toString)
        WaitingTimes.refreshCrime(user, nextCrime)
      }
      case None => crimeTime
    }
  }

}