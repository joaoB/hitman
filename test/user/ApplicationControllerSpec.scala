package user

import play.api.test.PlaySpecification
import org.specs2.mock.Mockito
import play.api.Environment
import play.api.i18n.{ DefaultLangs, DefaultMessagesApi }
import play.api.libs.json.Json
import play.api.mvc._
import play.api.test._

import scala.concurrent.Future
import service.UserService
import model.User
import controllers.ApplicationController
import scalaz.concurrent.Timeout
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class ApplicationControllerSpec extends PlaySpecification with Mockito with Results {

  implicit val mockedService: UserService = mock[UserService]

  "EmployeeController " should {

    "create a employee" in new WithEmpApplication() {
      //val user = User(0, "a", 0, 0, 1, 1)
      
      mockedService.doCrime(11) returns Future.successful("You did")
      val result = appController.doCrime(11).apply(FakeRequest())
      val resultAsString = contentAsString(result)
      resultAsString must contain("You did")
    }
  }

  class WithEmpApplication(implicit mockedService: UserService) extends WithApplication {
    val appController = new ApplicationController(mockedService)
  }
}