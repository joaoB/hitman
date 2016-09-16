package services.crime

import scala.concurrent.Future
import model.User


abstract class CrimeServiceBase {
  val crimeTime: Int
  def crimeAmount: Int
  def canPerform(user: User): Future[Long]
  def refresh(user: User)

}