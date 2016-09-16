package model

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import java.sql.Timestamp
import java.util.Date

import model.Users;

case class WaitingTime(id: Long, bullets: Timestamp, crime: Timestamp, user: Long)

class WaitingTimeTableDef(tag: Tag) extends Table[WaitingTime](tag, "waitingTime") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def bullets = column[Timestamp]("bullets")
  def crime = column[Timestamp]("crime")
  def user = column[Long]("userId")
  def userId = foreignKey("userId", user, Users.users)(_.id)

  override def * =
    (id, bullets, crime, user) <> (WaitingTime.tupled, WaitingTime.unapply)
}

object WaitingTimes {

  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  val waitingTimes = TableQuery[WaitingTimeTableDef]

  def add(wt: WaitingTime): Future[Boolean] = {
    dbConfig.db.run(waitingTimes += wt).map(
      res => true).recover {
        case ex: Exception => false
      }
  }

  def getByUser(user: User): Future[Option[WaitingTime]] = {
    dbConfig.db.run(waitingTimes.filter(_.user === user.id).result.headOption)
  }

  def updateByUser(user: User, waitingToUpdate: WaitingTime): Future[Int] = {
    dbConfig.db.run(waitingTimes.filter(_.user === user.id).update(waitingToUpdate))
  }

  def refreshCrime(user: User, nextCrime: Timestamp): Future[Int] = {
    getByUser(user) flatMap {
      case Some(elem) => {
        val waitingToUpdate = elem.copy(crime = nextCrime)
        updateByUser(user, waitingToUpdate)
      }
      case None => Future(0) //something went really bad
    }
  }

}