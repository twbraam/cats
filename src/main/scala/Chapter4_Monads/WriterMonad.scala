package Chapter4_Monads

import cats.syntax.writer._ // for writer
import cats.instances.vector._ // for Monoid
import cats.syntax.applicative._ // for pure
import cats.data.Writer
import cats.instances.vector._ // for Monoid s

object WriterMonad {
  type Logged[A] = Writer[Vector[String], A]

  val writer1 = for {
    a <- 10.pure[Logged]
    _ <- Vector("a", "b", "c").tell
    b <- 32.writer(Vector("x", "y", "z"))
  } yield a + b
  // writer1: cats.data.WriterT[cats.Id,Vector[String],Int] = WriterT((Vector(a, b, c, x, y, z),42))
  val poep = writer1.run
  // res4: cats.Id[(Vector[String], Int)] = (Vector(a, b, c, x, y, z),42)

  val writer2 = writer1.mapWritten(_.map(_.toUpperCase))
  // writer2: cats.data.WriterT[cats.Id,scala.collection.immutable.Vector[String],Int] = WriterT((Vector(A, B, C, X, Y, Z),42))
  val poep2 = writer2.run
  // res5: cats.Id[(scala.collection.

  val writer3 = writer1.bimap(
    log => log.map(_.toUpperCase),
    res => res * 100
  )
  // writer3: cats.data.WriterT[cats.Id,scala.collection.immutable.Vector[String],Int] = WriterT((Vector(A, B, C, X, Y, Z),4200))
  val poep3 = writer3.run
  // res6: cats.Id[(scala.collection.immutable.Vector[String], Int)] = (Vector(A, B, C, X, Y, Z),4200)
  val writer4 = writer1.mapBoth { (log, res) =>
    val log2 = log.map(_ + "!")
    val res2 = res * 1000
    (log2, res2)
  }
  // writer4: cats.data.WriterT[cats.Id,scala.collection.immutable.Vector[String],Int] = WriterT((Vector(a!, b!, c!, x!, y!, z!),42000))
  val poep4 = writer4.run
  // res7: cats.Id[(scala.collection.immutable.Vector[String], Int)] = (Vector(a!, b!, c!, x!, y!, z!),42000)
}