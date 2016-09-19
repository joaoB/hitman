package modules

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

import com.google.inject.AbstractModule
import com.google.inject._
import com.google.inject.name.Names

import akka.actor.Actor
import akka.actor.Props
import akka.actor.actorRef2Scala
import service.UserService
import services.crime.BulletsService

object HelloActor {
  def props = Props[HelloActor]

  case class SayHello(name: String)
}

class HelloActor(bulletsService: BulletsService) extends Actor {
  import HelloActor._

  def receive = {
    case SayHello(name: String) => {
      import scala.concurrent.ExecutionContext.Implicits.global
      println("for now just this " + bulletsService.actionTime)
      //sender() ! "Hello, " + name
    }
  }
}