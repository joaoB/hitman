package services.crime

import java.sql.Timestamp
import java.util.Date

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.google.inject._

import model.User
import model.Users
import model.WaitingTimes
import java.util.Calendar
import model.WaitingTime
import service.WaitingTimeService

class BulletsService @Inject() (
    usersRepository: Users,
    waitingTimeService: WaitingTimeService) extends GenericActionService(waitingTimeService) {
  val actionTime: Int = 60 * 60 * 1000

  def doAction(user: User) = ??? //think about this later

  def doAction(user: User, amount: Int): Future[String] =
    if (amount <= 0) {
      Future("You have to buy an amount greater than 0 bullets!")
    } else {
      whenNextAction(user) map {
        case time if time < 0 => {
          setHot(user)
          usersRepository.buyBullets(user, amount)
          "Success! You bought " + amount + " bullets"
        }
        case time => "You still have to wait " + (time / 1000).toInt + " seconds!"
      }
    }

  def refresh = {
    (elem: WaitingTime, t: Timestamp) => waitingTimeService.refreshBullets(elem, t)
  }

  def whenNextActionAux(elem: WaitingTime) = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    Future(elem.bullets.getTime - now.getTime toInt)
  }
}