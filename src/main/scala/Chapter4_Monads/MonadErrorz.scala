package Chapter4_Monads
import cats.MonadError
import cats.instances.either._ // for MonadError
import cats.syntax.applicative._ // for pure
import cats.syntax.applicativeError._ // for raiseError etc
import cats.syntax.monadError._ // for ensure

object MonadErrorz {

  type ErrorOr[A] = Either[String, A]
  val monadError = MonadError[ErrorOr, String]

  val success = monadError.pure(42)
  // success: ErrorOr[Int] = Right(42)

  val failure = monadError.raiseError("Badness")
  // failure: ErrorOr[Nothing] = Left(Badness)

  monadError.handleError(failure) {
    case "Badness" =>
      monadError.pure("It's ok")
    case other =>
      monadError.raiseError("It's not ok")
    }
    // res2: ErrorOr[ErrorOr[String]] = Right(Right(It's ok))


  val success2 = 42.pure[ErrorOr]
  // success: ErrorOr[Int] = Right(42)
  val failure2 = "Badness".raiseError[ErrorOr, Int]
  // failure: ErrorOr[Int] = Left(Badness)
  success.ensure("Number to low!")(_ > 1000)
  // res4: Either[String,Int] = Left(Number to low!)
}


