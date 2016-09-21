package user

import scala.concurrent.Future

import org.scalatest._
import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import service.UserService
import controllers.ApplicationController
import scala.concurrent.ExecutionContext.Implicits.global
import service.UserService
import com.google.inject._
import play.api.http.Writeable
import play.api.inject.guice.GuiceApplicationBuilder

class ExampleControllerSpec extends PlaySpecification with Results {
  "Example Page#index" should {

    lazy val app = new GuiceApplicationBuilder().build
    val basicHeaders = Headers(
      "type" -> "application/json")
    def routeGET(uri: String) = getRoute(GET, uri, AnyContentAsEmpty)
    def getRoute[A](method: String, uri: String, body: A)(implicit w: Writeable[A]) = route(app, FakeRequest(method, uri, basicHeaders, body)).get

    def codeMustMatch(code: Int, result: Future[Result]) = {
      status(result) must equalTo(code)
    }

    s"warn if header is not present" in {
      codeMustMatch(200, routeGET("/"))
    }

    s"warn if header is not present" in {
      val result = routeGET("/")
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Registered Users")
    }
  }
}
