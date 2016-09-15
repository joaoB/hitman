package model

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

case class User(id: Long, username: String, hp: Int, rp: Float, bullets: Int, money: Int)

class UserTableDef(tag: Tag) extends Table[User](tag, "user") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def hp = column[Int]("hp")
  def rp = column[Float]("rp")
  def bullets = column[Int]("bullets")
  def money = column[Int]("money")

  override def * =
    (id, username, hp, rp, bullets, money) <> (User.tupled, User.unapply)
}

object Users {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val users = TableQuery[UserTableDef]

  def add(user: User): Future[Int] = {
    dbConfig.db.run(users += user).map(
      res => res).recover {
        case ex: Exception => -1
      }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(users.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(users.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[User]] = {
    dbConfig.db.run(users.result)
  }

  def buyBullets(user: User, amount: Int) = {
    val userToUpdate = user.copy(bullets = user.bullets + amount)
    dbConfig.db.run(users.filter(_.id === user.id).update(userToUpdate))
  }

  def addMoney(user: User, prize: Int) = {
    val userToUpdate = user.copy(money = user.money + prize)
    dbConfig.db.run(users.filter(_.id === user.id).update(userToUpdate))

  }

}
