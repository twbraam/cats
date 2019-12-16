package Chapter3_Functors

import cats.syntax.functor._
import cats.Functor

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object TreeFunctor extends App {
  implicit def leafFunctor: Functor[Tree] =
    new Functor[Tree] {
      def map[A, B](tree: Tree[A])(func: A => B): Tree[B] = tree match {
        case Branch(left, right) =>
          Branch(map(left)(func), map(right)(func))
        case Leaf(value) =>
          Leaf(func(value))
      }
    }
  object Tree {
    def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
      Branch(left, right)
    def leaf[A](value: A): Tree[A] =
      Leaf(value)
  }    
  
  Tree.leaf(45).map(_ + 3)
}