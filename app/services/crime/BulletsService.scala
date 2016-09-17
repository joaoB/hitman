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

class BulletsService @Inject() (
    usersRepository: Users,
    waitingTimeRepository: WaitingTimes) extends GenericActionService(waitingTimeRepository) {
  val actionTime: Int = 60 * 60 * 60 * 1000

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

  def setHot(user: User): Future[Int] = {
    waitingTimeRepository.getByUser(user) flatMap {
      case Some(elem) =>
        waitingTimeRepository.refreshBullets(user, super.calculateNextActionTime)
      case None => Future(-1) //something went bad
    }
  }

  def whenNextActionAux(elem: WaitingTime) = {
    val now = new Timestamp(Calendar.getInstance.getTime.getTime)
    elem.bullets.getTime - now.getTime
  }
}