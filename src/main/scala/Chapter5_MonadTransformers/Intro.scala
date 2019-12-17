package Chapter5_MonadTransformers

import cats.Monad
import cats.instances.list._ // for Monad
import cats.syntax.applicative._ // for pure
import cats.data.OptionT


object Intro {
  type ListOption[A] = OptionT[List, A]
  val result1: ListOption[Int] = OptionT(List(Option(10)))
  // result1: ListOption[Int] = OptionT(List(Some(10)))
  val result2: ListOption[Int] = 32.pure[ListOption]
  // result2: ListOption[Int] = OptionT(List(Some(32)))


  result1.flatMap { (x: Int) =>
    result2.map { (y: Int) =>
      x + y
    }
  }
  // res1: cats.data.OptionT[List,Int] = OptionT(List(Some(42)))


  // Alias Either to a type constructor with one parameter:
  type ErrorOr[A] = Either[String, A]
  // Build our final monad stack using OptionT:
  type ErrorOrOption[A] = OptionT[ErrorOr, A]
  import cats.instances.either._ // for Monad
  val a = 10.pure[ErrorOrOption]
  // a: ErrorOrOption[Int] = OptionT(Right(Some(10)))
  val b = 32.pure[ErrorOrOption]
  // b: ErrorOrOption[Int] = OptionT(Right(Some(32)))
  val c = a.flatMap(x => b.map(y => x + y))
  // c: cats.data.OptionT[ErrorOr,Int] = OptionT(Right(Some(42)))



  // Depth of three: a Future of an Either of Option
  import scala.concurrent.Future
  import cats.data.{EitherT, OptionT}
  type FutureEither[A] = EitherT[Future, String, A]
  type FutureEitherOption[A] = OptionT[FutureEither, A]

  import cats.instances.future._ // for Monad
  import scala.concurrent.Await
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  val futureEitherOr: FutureEitherOption[Int] =
    for {
    a <- 10.pure[FutureEitherOption]
    b <- 32.pure[FutureEitherOption]
  } yield a + b



  // Create using apply:
  val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10)))
  // errorStack1: cats.data.OptionT[ErrorOr,Int] = OptionT(Right(Some(10)))
  // Create using pure:
  val errorStack2 = 32.pure[ErrorOrOption]
  // errorStack2: ErrorOrOption[Int] = OptionT(Right(Some(32)))



  // Unpacking:
  // futureEitherOr = 
  // res14: FutureEitherOption[Int] = OptionT(EitherT(Future(Success(Right(Some(42))))))
  val intermediate = futureEitherOr.value
  // intermediate: FutureEither[Option[Int]] = EitherT(Future(Success(Right(Some(42)))))
  val stack = intermediate.value
  // stack: scala.concurrent.Future[Either[String,Option[Int]]] = Future(Success(Right(Some(42))))
  Await.result(stack, 1.second)
  // res15: Either[String,Option[Int]] = Right(Some(42))
}