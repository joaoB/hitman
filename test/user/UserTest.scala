package user

import org.scalatest.FunSuite
import model.User
import service.UserService

class UserTest extends FunSuite {

  test("Creation of users") {
    val u = User(0, "username", 0, 0, 0)
    
  }

}

