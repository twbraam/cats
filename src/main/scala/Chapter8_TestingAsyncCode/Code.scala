package Chapter8_TestingAsyncCode
import cats.Applicative

object Code {
  import scala.concurrent.Future
  import cats.Id

  trait UptimeClient[F[_]] {
    def getUptime(hostname: String): F[Int]
  }

  

  import cats.instances.future._ // for Applicative
  import cats.instances.list._ // for Traverse
  import cats.syntax.traverse._ // for traverse
  import cats.Applicative
  import cats.syntax.functor._ // for map

  import scala.concurrent.ExecutionContext.Implicits.global
  class UptimeService[F[_]](client: UptimeClient[F])(implicit a: Applicative[F]) {
    def getTotalUptime(hostnames: List[String]): F[Int] =
      hostnames.traverse(client.getUptime).map(_.sum) 
  }

  trait RealUptimeClient extends UptimeClient[Future] {
    def getUptime(hostname: String): Future[Int]
  }

  class TestUptimeClient(hosts: Map[String, Int]) extends UptimeClient[Id] {
    def getUptime(hostname: String): Int =
      hosts.getOrElse(hostname, 0)
  }

  
  def testTotalUptime() = {
    val hosts = Map("host1" -> 10, "host2" -> 6)
    val client = new TestUptimeClient(hosts)
    val service = new UptimeService(client)
    val actual = service.getTotalUptime(hosts.keys.toList)
    val expected = hosts.values.sum
    assert(actual == expected)
  }

}