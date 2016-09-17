package model

import java.sql.Timestamp

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import com.google.inject._

import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

case class WaitingTime(id: Long, bullets: Timestamp, crime: Timestamp, user: Long)

@Singleton
class WaitingTimes @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import driver.api._

  private class WaitingTimeTableDef(tag: Tag) extends Table[WaitingTime](tag, "waitingTime") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def bullets = column[Timestamp]("bullets")
    def crime = column[Timestamp]("crime")
    def user = column[Long]("userId")
    //def userId = foreignKey("userId", user, Users.users)(_.id)

    override def * =
      (id, bullets, crime, user) <> (WaitingTime.tupled, WaitingTime.unapply)
  }

  private val waitingTimes = TableQuery[WaitingTimeTableDef]

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

  def refreshBullets(user: User, next: Timestamp): Future[Int] = {
    getByUser(user) flatMap {
      case Some(elem) => {
        val waitingToUpdate = elem.copy(bullets = next)
        updateByUser(user, waitingToUpdate)
      }
      case None => Future(0) //something went really bad
    }
  }

}
