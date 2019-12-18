package Chapter5_MonadTransformers

import cats.data.EitherT
import scala.concurrent.Future
import cats.instances.future._ // for Monad
import cats.syntax.flatMap._ // for flatMap
import scala.concurrent.ExecutionContext.Implicits.global

object Exercise {
  type Response[A] = EitherT[Future, String, A]
  // defined type alias Response



  val powerLevels = Map(
    "Jazz" -> 6,
    "Bumblebee" -> 8,
    "Hot Rod" -> 10
  )

  def getPowerLevel(autobot: String): Response[Int] = powerLevels.get(autobot) match {
    case Some(avg) => EitherT.right(Future(avg))
    case None => EitherT.left(Future(s"Could not reach $autobot"))
  }

  def canSpecialMove(ally1: String, ally2: String): Response[Boolean] = for {
    level1 <- getPowerLevel(ally1)
    level2 <- getPowerLevel(ally2)
  } yield (level1 + level2) > 15
    
  
  import scala.concurrent.duration._
  import scala.concurrent.Await
  
  def tacticalReport(ally1: String, ally2: String): String = {
    val stack = canSpecialMove(ally1, ally2).value 
    
    Await.result(stack, 1.second) match {
      case Left(msg) => s"Comms error: $msg"
      case Right(true) => s"$ally1 and $ally2 are ready to roll out!"
      case Right(false) => s"$ally1 and $ally2 need a recharge."
    }
  }
}