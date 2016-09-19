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

@Singleton
class BulletsService @Inject() (
    usersRepository: Users,
    waitingTimeService: WaitingTimeService) extends GenericActionService(waitingTimeService) {

  val actionTime: Int = 60 * 60 * 1000

  def doAction(user: User) = ??? //think about this later

  def doActionAux(user: User, amount: Int): Future[String] = {
    waitingTimeService.getWaitingTimeByUser(user, {
      wt =>
        {
          whenNextAction(wt).fold(
            nextActionMessage => nextActionMessage,
            totalPrice => {
              userHasMoney(user, amount) match {
                case Left(noCash) => noCash
                case Right(totalPrice) => {
                  refresh(wt, super.calculateNextActionTime)
                  usersRepository.buyBullets(user, amount)
                  usersRepository.addMoney(user, -totalPrice)
                  "Success! You bought " + amount + " bullets"
                }
              }

            })
        }
    })
  }

  private def userHasMoney(user: User, amount: Int) : Either[String, Int] = {
    val totalPrice = amount * 30 // 30 is the price per bullet, make this a service later
    if (user.money > totalPrice) {
      Right(totalPrice)
    } else {
      Left("You do not have enough cash!")
    }
  }

  def doAction(user: User, amount: Int): Future[String] =
    amount match {
      case amount if amount <= 0 => Future("You have to buy an amount greater than 0 bullets!")
      case _                     => doActionAux(user, amount)
    }

  def refresh(elem: WaitingTime, t: Timestamp) =
    waitingTimeService.refreshBullets(elem, t)

  def getTimeOfNextAction(elem: WaitingTime) = elem.bullets.getTime

}