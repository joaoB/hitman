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
import akka.actor.Props
import modules.HelloActor
import service.UserService

@Singleton
class BulletsService @Inject() (
  usersRepository: Users,
  waitingTimeService: WaitingTimeService)
    extends GenericActionService(waitingTimeService) {

  val system = akka.actor.ActorSystem("system")
  val testActor = system.actorOf(Props(new HelloActor(this)), "hello-actor")
  import scala.concurrent.duration._
  //val cancellable = system.scheduler.schedule(
  //0.microseconds, 1.second, testActor, HelloActor.SayHello("a"))

  val actionTime: Int = 60 * 60 * 1000

  def doAction(user: User) = ??? //think about this later

  private def finalizeBuyBullets(wt: WaitingTime, user: User, amount: Int, totalPrice: Int): String = {
    refresh(wt, super.calculateNextActionTime)
    usersRepository.buyBullets(user, amount, totalPrice)
    "Success! You bought " + amount + " bullets"
  }

  private def buyBullets(user: User, amount: Int, wt: WaitingTime): String = {
    val result = for {
      nextAction <- whenNextAction(wt).right
      totalPrice <- BuyBulletsValidator(user, amount).validate.right
    } yield totalPrice

    result.fold(
      err => err,
      totalPrice => finalizeBuyBullets(wt, user, amount, totalPrice))
  }

  def doAction(user: User, amount: Int): Future[String] = {
    waitingTimeService.getWaitingTimeByUser(user, {
      wt => buyBullets(user, amount, wt)
    })
  }

  def refresh(elem: WaitingTime, t: Timestamp) =
    waitingTimeService.refreshBullets(elem, t)

  def getTimeOfNextAction(elem: WaitingTime) = elem.bullets.getTime

}
