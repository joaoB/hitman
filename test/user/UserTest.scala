package user

import org.scalatestplus.play._
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.Matchers._ //when should
import scala.collection.mutable
import service.UserService
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import model.User
import scala.concurrent._
import org.scalatest.concurrent._

class StackSpec extends PlaySpec with MockitoSugar  with ScalaFutures {

  "A Stack" must {
    "pop values in last-in-first-out order" in {
      val mockDataService = mock[UserService]
      val user = User(0, "fiona", 0, 0, 0, 0)
      mockDataService.addUser(user)
      mockDataService.listAllUsers
      //      when(mockDataService.listAllUsers) thenReturn Data(new java.util.Date())

      when(mockDataService.listAllUsers) thenReturn Future(user :: Nil)
      //when(mockDataService.addUser(user)) thenReturn user.id

      
      val users = mockDataService.listAllUsers
      whenReady(users) { result =>
        result.length mustBe 1
      }

    }
    "throw NoSuchElementException if an empty stack is popped" in {
      val emptyStack = new mutable.Stack[Int]
      a[NoSuchElementException] must be thrownBy {
        emptyStack.pop()
      }
    }
  }
}