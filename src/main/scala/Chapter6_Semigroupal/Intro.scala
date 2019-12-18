package Chapter6_Semigroupal

object Intro {
  import cats.Semigroupal
  import cats.instances.option._ // for Semigroupal
  Semigroupal[Option].product(Some(123), Some("abc"))
  // res0: Option[(Int, String)] = Some((123,abc))

  Semigroupal[Option].product(None, Some("abc"))
  // res1: Option[(Nothing, String)] = None
  Semigroupal[Option].product(Some(123), None)
  // res2: Option[(Int, Nothing)] = None

  Semigroupal.tuple3(Option(1), Option(2), Option(3))
  // res3: Option[(Int, Int, Int)] = Some((1,2,3))
  Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int])
  // res4: Option[(Int, Int, Int)] = None

  Semigroupal.map3(Option(1), Option(2), Option(3))(_ + _ + _)
  // res5: Option[Int] = Some(6)
  Semigroupal.map2(Option(1), Option.empty[Int])(_ + _)
  // res6: Option[Int] = None

  import cats.syntax.apply._ // for tupled and mapN
  (Option(123), Option("abc")).tupled
  // res7: Option[(Int, String)] = Some((123,abc))
  (Option(123), Option("abc"), Option(true)).tupled
  // res8: Option[(Int, String, Boolean)] = Some((123,abc,true))



  case class Cat(name: String, born: Int, color: String)

  (Option("Garfield"),
    Option(1978),
    Option("Orange & black")).mapN(Cat.apply)
  // res9: Option[Cat] = Some(Cat(Garfield,1978,Orange & black))


  import cats.Monoid
  import cats.instances.int._ // for Monoid
  import cats.instances.invariant._ // for Semigroupal
  import cats.instances.list._ // for Monoid
  import cats.instances.string._ // for Monoid
  import cats.syntax.apply._ // for imapN

  case class Cat2(name: String, yearOfBirth: Int, favoriteFoods: List[String])
  val tupleToCat: (String, Int, List[String]) => Cat2 =
    Cat2.apply
  val catToTuple: Cat2 => (String, Int, List[String]) =
    cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)
  implicit val catMonoid: Monoid[Cat2] =
    (Monoid[String], Monoid[Int], Monoid[List[String]])
      .imapN(tupleToCat)(catToTuple)

  import cats.syntax.semigroup._ // for |+|
  val garfield = Cat2("Garfield", 1978, List("Lasagne"))
  val heathcliff = Cat2("Heathcliff", 1988, List("Junk Food"))
  garfield |+| heathcliff
  // res17: Cat = Cat(GarfieldHeathcliff,3966,List(Lasagne, Junk Food))



  // Future
  import cats.Semigroupal
  import cats.instances.future._ // for Semigroupal
  import scala.concurrent._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global
  val futurePair: Future[(String, Int)] = Semigroupal[Future].
    product(Future("Hello"), Future(123))
  Await.result(futurePair, 1.second)
  // res1: (String, Int) = (Hello,123)

  import cats.syntax.apply._ // for mapN
  val futureCat: Future[Cat2] = (Future("Garfield"), Future(1978), Future(List("Lasagne")))
    .mapN(Cat2.apply)
  Await.result(futureCat, 1.second)
  // res4: Cat = Cat(Garfield,1978,List(Lasagne))


  // Unexpected:
  // List
  import cats.Semigroupal
  import cats.instances.list._ // for Semigroupal
  Semigroupal[List].product(List(1, 2), List(3, 4))
  // res5: List[(Int, Int)] = List((1,3), (1,4), (2,3), (2,4))

  // Either
  import cats.instances.either._ // for Semigroupal
  type ErrorOr[A] = Either[Vector[String], A]
  Semigroupal[ErrorOr].product(
    Left(Vector("Error 1")),
    Left(Vector("Error 2"))
  )
  // res7: ErrorOr[(Nothing, Nothing)] = Left(Vector(Error 1))



  
}
