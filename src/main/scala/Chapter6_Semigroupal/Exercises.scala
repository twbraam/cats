package Chapter6_Semigroupal

import cats.Monad
import cats.syntax.either._ // for catchOnly
import cats.data.Validated
import cats.syntax.validated._ // for valid and invalid
import cats.syntax.apply._ // for tupled

import cats.instances.list._ // for Monoid

object Exercises {
  

  // 6.4.4: validation 
  type FormData = Map[String, String]
  type FailFast[A] = Either[List[String], A]
  type FailSlow[A] = Validated[List[String], A]

  case class User(name: String, age: Int)

  def getValue(name: String)(data: FormData): FailFast[String] =
    data.get(name)
      .toRight(List(s"$name field not specified"))


  def parseInt(input: String): FailFast[Int] =
    Either.catchOnly[NumberFormatException](input.toInt).
      leftMap(_ => List(s"'age' must be an integer"))
  

  def nonBlank(name: String)(data: String): FailFast[String] =
    Right(name).ensure(List(s"$data cannot be blank"))(_.nonEmpty)

  def nonNegative(entry: Int): FailFast[Int] =
    Right(entry).ensure(List("Age has to be higher than 0"))(_ >= 0)

  
  def readName(data: FormData): FailFast[String] =
    getValue("name")(data)
      .flatMap(nonBlank("name"))

  def readAge(data: FormData): FailFast[Int] = 
    getValue("age")(data)
    .flatMap(nonBlank("age"))
    .flatMap(parseInt)
    .flatMap(nonNegative)

  def createUser(data: FormData): FailSlow[User] =
    (readName(data).toValidated, readAge(data).toValidated)
      .mapN(User.apply)
}