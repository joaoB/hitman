package user

import play.api.Application
import play.api.test.{ WithApplication, PlaySpecification }
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import java.util.Date
import scala.concurrent.Future
import services.crime.BulletsService
import model.User
import junit.framework.Assert

class BulletServiceSpec extends PlaySpecification {

  import models._

  "Bullet Service" should {

    def bulletService(implicit app: Application) = Application.instanceCache[BulletsService].apply(app)

    "validate if user does not have enough cash" in new WithApplication() {
      val user = User(11, "fake", 0, 0, 0, 0)
      val amount = 1000
      val result = await(bulletService.doAction(user, amount))
      Assert.assertTrue(result.contains("You do not have enough cash!"))
    }
    "validate max bullets amount" in new WithApplication() {
      val user = User(11, "username", 0, 0, 0, 0)
      val amount = 1001
      val result = await(bulletService.doAction(user, amount))
      Assert.assertTrue(result.contains("You can only buy 1000 bullets!"))
    }
    "validate min bullets amount" in new WithApplication() {
      val user = User(11, "username", 0, 0, 0, 0)
      val amount = 0
      val result = await(bulletService.doAction(user, amount))
      Assert.assertTrue(result.contains("You have to buy an amount of bullets greater than zero!"))
    }

  }

  def await[T](v: Future[T]): T = Await.result(v, Duration.Inf)
}