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

  val waitingTimes = TableQuery[UserTableDef]

}
