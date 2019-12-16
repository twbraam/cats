package Chapter3_Functors

import cats.Functor
import cats.instances.list._ // for Functor
import cats.instances.option._ // for Functor
import cats.instances.function._
import cats.syntax.functor._

object Intro {
  val list1 = List(1, 2, 3)
  // list1: List[Int] = List(1, 2, 3)
  val list2 = Functor[List].map(list1)(_ * 2)
  // list2: List[Int] = List(2, 4, 6)
  val option1 = Option(123)
  // option1: Option[Int] = Some(123)
  val option2 = Functor[Option].map(option1)(_.toString)
  // option2: Option[String] = Some(123)

  val func = (x: Int) => x + 1
  // func: Int => Int = <function1>
  val liftedFunc = Functor[Option].lift(func)
  liftedFunc(Option(1))
  // res0: Option[Int] = Some(2)


  val func1 = (a: Int) => a + 1
  val func2 = (a: Int) => a * 2
  val func3 = (a: Int) => a + "!"
  val func4 = func1.map(func2).map(func3)
  func4(123)
  // res1: String = 248!


  def doMath[F[_]](start: F[Int])
      (implicit functor: Functor[F]): F[Int] =
    start.map(n => n + 1 * 2)

  doMath(Option(20))
  // res3: Option[Int] = Some(22)
  doMath(List(1, 2, 3))
  // res4: List[Int] = List(3, 4, 5)


import scala.concurrent.{Future, ExecutionContext}

implicit def futureFunctor
    (implicit ec: ExecutionContext): Functor[Future] =
  new Functor[Future] {
    def map[A, B](value: Future[A])(func: A => B): Future[B] =
      value.map(func)
    }

}