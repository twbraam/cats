package Chapter1

import cats._
import cats.implicits._
import cats.instances.int._
import cats.instances.option._ // for Eq
import cats.syntax.eq._

object EqInstances {
  implicit val catEq: Eq[Cat] =
    Eq.instance[Cat] { (cat1, cat2) =>
      cat1.age == cat2.age && cat1.name == cat2.name && cat1.color == cat2.color
    }
}
