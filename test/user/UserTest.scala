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

    s"Index is available" in {
      codeMustMatch(200, routeGET("/"))
    }

    s"Index returns correct response" in {
      val result = routeGET("/")
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Registered Users")
    }

    s"Crime returns invalid for invalid user" in {
      val result = routeGET("/doCrime/0")
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Invalid user")
    }

    s"Bullets returns invalid for invalid user" in {
      val result = routeGET("/buyBullets/0/1000")
      status(result) must equalTo(OK)
      contentAsString(result) must contain("Invalid user")
    }

//    s"Reset response times" in {
//      val result = routeGET("/reset/11")
//      status(result) must equalTo(OK)
//      contentAsString(result) must contain("Times reseted for user 11")
//    }
//
//    s"Correct response when buying to much bullets" in {
//      val result = routeGET("/buyBullets/11/1001")
//      status(result) must equalTo(OK)
//      contentAsString(result) must contain("You have to buy an amount of bullets greater than zero and less than 1000!")
//    }
//
//    s"Correct response when buying under bullets" in {
//      val result = routeGET("/buyBullets/11/0")
//      status(result) must equalTo(OK)
//      contentAsString(result) must contain("You have to buy an amount of bullets greater than zero and less than 1000!")
//    }
//
//    s"Correct response when you do not have enough cash" in {
//      val result = routeGET("/buyBullets/11/1000")
//      status(result) must equalTo(OK)
//      contentAsString(result) must contain("You do not have enough cash!")
//    }
//
//    s"Correct response when you buy bullets" in {
//      val result = routeGET("/buyBullets/11/1")
//      status(result) must equalTo(OK)
//      contentAsString(result) must contain("Success")
//    }
//
//    s"Reset response times" in {
//      val result = routeGET("/reset/11")
//      status(result) must equalTo(OK)
//      contentAsString(result) must contain("Times reseted for user 11")
//    }
//
//    s"Correct response when you buy bullets after reset" in {
//      val result = routeGET("/buyBullets/11/1")
//      status(result) must equalTo(OK)
//      contentAsString(result) must contain("Success")
//    }
//
//    s"Correct response (give waiting time) when trying to buy when hot" in {
//      val result = routeGET("/buyBullets/11/0")
//      status(result) must equalTo(OK)
//      contentAsString(result) must contain("You still have to wait ")
//    }
  }
}
