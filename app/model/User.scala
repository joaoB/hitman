package model

import scala.concurrent.{ Future, ExecutionContext }
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import com.google.inject._

case class User(id: Long, username: String, hp: Int, rp: Float, bullets: Int, money: Int)

@Singleton
class Users @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class UserTableDef(tag: Tag) extends Table[User](tag, "user") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def hp = column[Int]("hp")
    def rp = column[Float]("rp")
    def bullets = column[Int]("bullets")
    def money = column[Int]("money")

    override def * =
      (id, username, hp, rp, bullets, money) <> (User.tupled, User.unapply)
  }

  private val users = TableQuery[UserTableDef]

  def add(user: User): Future[Long] = {
    dbConfig.db.run((users returning users.map(_.id)) += user)
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
  
  def buyBullets(user: User, bulletsAmount: Int, price: Int) = {
    val userToUpdate = user.copy(bullets = user.bullets + bulletsAmount, money = user.money - price)
    update(userToUpdate)
  }

  def addMoney(user: User, prize: Int) = {
    val userToUpdate = user.copy(money = user.money + prize)
    update(userToUpdate)
  }

  def update(userToUpdate: User) = {
    dbConfig.db.run(users.filter(_.id === userToUpdate.id).update(userToUpdate))
  }
  
}
