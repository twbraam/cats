package Chapter2_Monoids

import cats.instances.option._
import cats.instances.int._
import cats.syntax.monoid._
import cats.Monoid

object Exercises extends App {
  def add[A: Monoid](items: List[A]): A = items.foldLeft(Monoid[A].empty)(_ |+| _) // or ->
  //def add[A](items: List[A])(implicit monoid: Monoid[A]): A = items.foldLeft(monoid.empty)(_ |+| _)


  implicit val orderMonoid: Monoid[Order] =
    new Monoid[Order] {
      def combine(x: Order, y: Order): Order = Order(x.totalCost + y.totalCost, x.quantity + y.quantity)
      def empty: Order = Order(0, 0)
    }

  println(add(List(Order(3, 2), Order(5, 4))))
}
