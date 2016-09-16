package services.crime

import java.sql.Timestamp
import java.util.Calendar
import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import model.User
import model.Users
import model.WaitingTime
import model.WaitingTimes
import com.google.inject._


class CrimeService @Inject() (usersRepository: Users, waitingTimeRepository: WaitingTimes){

  val crimeTime = 60 * 1000

  def crimeAmount: Int = {
    Math.random * 1000 toInt
  }

  def canPerform(user: User): Future[Long] = {
    waitingTimeRepository.getByUser(user) map {
      case Some(elem) => canPerformAux(elem)
      case None       => crimeTime
    }
  }

  def doCrime(user: User): Future[String] = {
    def doCrimeAux(user: User, time: Long): Future[String] = {
      if (time < 0) {
        val prize = crimeAmount
        usersRepository.addMoney(user, prize)
        refresh(user)
        Future("You did " + prize)
      } else {
        Future("You still have to wait " + (time / 1000).toInt + " seconds!")
      }
    }
    for {
      time <- canPerform(user)
      result <- doCrimeAux(user, time)
    } yield result
  }

  private def canPerformAux(elem: WaitingTime) = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    elem.crime.getTime - now.getTime
  }

  def refresh(user: User) = {
    waitingTimeRepository.getByUser(user) map {
      case Some(elem) =>
        waitingTimeRepository.refreshCrime(user, calculateNextCrimeTime)
      case None =>
    }
  }

  private def calculateNextCrimeTime = {
    val d = new Date
    d.setTime(d.getTime + crimeTime)
    new Timestamp(d.getTime)
  }

}