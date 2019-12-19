package Chapter7_Foldable

object IntroTraverse extends App {
  import scala.concurrent._
  import scala.concurrent.duration._
  import scala.concurrent.ExecutionContext.Implicits.global
  val hostnames = List(
    "alpha.example.com",
    "beta.example.com",
    "gamma.demo.com"
  )
  def getUptime(hostname: String): Future[Int] =
    Future(hostname.length * 60) // just for demonstration

  // using foldLeft
  val allUptimes: Future[List[Int]] =
    hostnames.foldLeft(Future(List.empty[Int])) { (accum, host) =>
      val uptime = getUptime(host)
        for {
          accum <- accum
          uptime <- uptime
        } yield accum :+ uptime
    }
    Await.result(allUptimes, 1.second)
    // res2: List[Int] = List(1020, 960, 840)

  // using traverse
  val allUptimes2: Future[List[Int]] =
    Future.traverse(hostnames)(getUptime)
    Await.result(allUptimes, 1.second)
    // res3: List[Int] = List(1020, 960, 840)





  // Future(List.empty[Int]) ==
  import cats.Applicative
  import cats.instances.future._ // for Applicative
  import cats.syntax.applicative._ // for pure
  List.empty[Int].pure[Future]

  def oldCombine(accum : Future[List[Int]], host : String):
      Future[List[Int]] = {
    val uptime = getUptime(host)
    for {
      accum <- accum
      uptime <- uptime
    } yield accum :+ uptime
  }

/*   import cats.syntax.apply._ // for mapN
  // Combining accumulator and hostname using an Applicative:
  def newCombine(accum: Future[List[Int]], host: String): Future[List[Int]] =
    (accum, getUptime(host)).mapN(_ :+ _)

  
  def listTraverse[F[_]: Applicative, A, B](list: List[A])(func: A => F[B]): F[List[B]] =
    list.foldLeft(List.empty[B].pure[F]) { (accum, item) =>
      (accum, func(item)).mapN(_ :+ _)
  }

  def listSequence[F[_]: Applicative, B](list: List[F[B]]): F[List[B]] =
    listTraverse(list)(identity)

  val totalUptime = listTraverse(hostnames)(getUptime)
  Await.result(totalUptime, 1.second)
    // res11: List[Int] = List(1020, 960, 840)

  import cats.instances.vector._ // for Applicative
  println(listSequence(List(Vector(1, 2), Vector(3, 4))))

  println(listSequence(List(Vector(1, 2), Vector(3, 4), Vector(5, 6))))

  println(listSequence(List(Future(1), Future(3), Future(5)))) */

  import cats.Traverse
  import cats.instances.future._ // for Applicative
  import cats.instances.list._ // for Traverse
  val totalUptime: Future[List[Int]] = Traverse[List].traverse(hostnames)(getUptime)
  Await.result(totalUptime, 1.second)
  // res1: List[Int] = List(1020, 960, 840)
  val numbers = List(Future(1), Future(2), Future(3))
  val numbers2: Future[List[Int]] = Traverse[List].sequence(numbers)
  Await.result(numbers2, 1.second)
  // res3: List[Int] = List(1, 2, 3)

  //synax:
  import cats.syntax.traverse._ // for sequence and traverse
  Await.result(hostnames.traverse(getUptime), 1.second)
  // res4: List[Int] = List(1020, 960, 840)
  Await.result(numbers.sequence, 1.second)
  // res5: List[Int] = List(1, 2, 3)
}
