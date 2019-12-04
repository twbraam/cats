package Chapter1

import PrintableSyntax._
import cats._
import cats.implicits._
import cats.instances.int._
import cats.instances.option._ // for Eq
import cats.syntax.eq._
import EqInstances._


object test extends App {
  val cat: Cat = Cat("Alice", 29, "Black")
  Printable.print(cat)


  cat.print

  implicit val dateShow: Show[Cat] = Show.show(cat => s"${cat.name.show} is a ${cat.age.show} year-old ${cat.color.show} cat.")
  println(cat.show)


  val cat1 = Cat("Garfield", 38, "orange and black")
  val cat2 = Cat("Heathcliff", 33, "orange and black")
  val optionCat1 = Option(cat1)
  val optionCat2 = Option.empty[Cat]

  cat1 === cat2
  cat1.some === cat2.some
  optionCat1 === optionCat2
}
