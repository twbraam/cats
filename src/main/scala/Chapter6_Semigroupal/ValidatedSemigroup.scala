package Chapter6_Semigroupal

object ValidatedSemigroup {
  // Validated
  import cats.Semigroupal
  import cats.data.Validated
  import cats.instances.list._ // for Monoid
  type AllErrorsOr[A] = Validated[List[String], A]
  Semigroupal[AllErrorsOr].product(
    Validated.invalid(List("Error 1")),
    Validated.invalid(List("Error 2"))
  )
  // res1: AllErrorsOr[(Nothing, Nothing)] = Invalid(List(Error 1, Error 2))
  val v = Validated.valid[List[String], Int](123)
  // v: cats.data.Validated[List[String],Int] = Valid(123)
  val i = Validated.invalid[List[String], Int](List("Badness"))
  // i: cats.data.Validated[List[String],Int] = Invalid(List(Badness))

  import cats.syntax.validated._ // for valid and invalid
  123.valid[List[String]]
  // res2: cats.data.Validated[List[String],Int] = Valid(123)
  List("Badness").invalid[Int]
  // res3: cats.data.Validated[List[String],Int] = Invalid(List(Badness))

  import cats.syntax.applicative._ // for pure
  import cats.syntax.applicativeError._ // for raiseError
  type ErrorsOr[A] = Validated[List[String], A]
  123.pure[ErrorsOr]
  // res5: ErrorsOr[Int] = Valid(123)
  List("Badness").raiseError[ErrorsOr, Int]
  // res6: ErrorsOr[Int] = Invalid(List(Badness))

  Validated.catchOnly[NumberFormatException]("foo".toInt)
  // res7: cats.data.Validated[NumberFormatException,Int] = Invalid(java.lang.NumberFormatException: For input string: "foo")
  Validated.catchNonFatal(sys.error("Badness"))
  // res8: cats.data.Validated[Throwable,Nothing] = Invalid(java.lang.RuntimeException: Badness)
  Validated.fromTry(scala.util.Try("foo".toInt))
  // res9: cats.data.Validated[Throwable,Int] = Invalid(java.lang.NumberFormatException: For input string: "foo")
  Validated.fromEither[String, Int](Left("Badness"))
  // res10: cats.data.Validated[String,Int] = Invalid(Badness)
  Validated.fromOption[String, Int](None, "Badness")
  // res11: cats.data.Validated[String,Int] = Invalid(Badness)

  
  // Combining
  import cats.instances.string._ // for Semigroup
  import cats.syntax.apply._ // for tupled
  ("Error 1".invalid[Int], "Error 2".invalid[Int]).tupled
  val x = "Error 1".invalid[Int]
  val y = (x, x).tupled

    // res14: cats.data.Validated[String,(Int, Int)] = Invalid(Error 1 Error 2)
  import cats.instances.vector._ // for Semigroupal
  (Vector(404).invalid[Int], Vector(500).invalid[Int]).tupled
  // res15: cats.data.Validated[scala.collection.immutable.Vector[Int],(Int, Int)] = Invalid(Vector(404, 500))


  // Methods
  123.valid.map(_ * 100)
  // res17: cats.data.Validated[Nothing,Int] = Valid(12300)
  "?".invalid.leftMap(_.toString)
  // res18: cats.data.Validated[String,Nothing] = Invalid(?)
  123.valid[String].bimap(_ + "!", _ * 100)
  // res19: cats.data.Validated[String,Int] = Valid(12300)
  "?".invalid[Int].bimap(_ + "!", _ * 100)
  // res20: cats.data.Validated[String,Int] = Invalid(?!)

  // flatMap substitution:
  32.valid.andThen { a =>
    10.valid.map { b =>
      a + b
    }
  }

  // convert to either
  import cats.syntax.either._ // for toValidated
  // import cats.syntax.either._
  "Badness".invalid[Int]
  // res22: cats.data.Validated[String,Int] = Invalid(Badness)
  "Badness".invalid[Int].toEither
  // res23: Either[String,Int] = Left(Badness)
  "Badness".invalid[Int].toEither.toValidated
  // res24: cats.data.Validated[String,Int] = Invalid(Badness)

  //etc. 
  123.valid[String].ensure("Negative!")(_ > 0)
  "fail".invalid[Int].getOrElse(0)
  // res26: Int = 0
  "fail".invalid[Int].fold(_ + "!!!", _.toString)
  // res27: String = fail!!!


}