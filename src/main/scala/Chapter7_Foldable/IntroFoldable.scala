package Chapter7_Foldable

object IntroFoldable extends App {
  import cats.Foldable
  import cats.instances.list._ // for Foldable
  val ints = List(1, 2, 3)
  Foldable[List].foldLeft(ints, 0)(_ + _)
  // res1: Int = 6

  import cats.instances.option._ // for Foldable
  val maybeInt = Option(123)
  Foldable[Option].foldLeft(maybeInt, 10)(_ * _)
  // res3: Int = 1230


  import cats.Eval
  import cats.Foldable
  def bigData = (1 to 100000).to(LazyList)
  import cats.instances.lazyList._ // for Foldable
  val eval: Eval[Long] = Foldable[LazyList]
    .foldRight(bigData, Eval.now(0L)) { (num, eval) =>
      eval.map(_ + num) }
  eval.value
  // res7: Long = 5000050000

  
  import cats.instances.int._ // for Monoid
  Foldable[List].combineAll(List(1, 2, 3))
  // res12: Int = 6

  import cats.instances.string._ // for Monoid
  Foldable[List].foldMap(List(1, 2, 3))(_.toString)
  // res13: String = 123

  import cats.instances.vector._ // for Monoid
  val ints2: List[Vector[Int]] = List(Vector(1, 2, 3), Vector(4, 5, 6))
  (Foldable[List] compose Foldable[Vector]).combineAll(ints2)
  // res15: Int = 21


  // syntax: 
  import cats.syntax.foldable._ // for combineAll and foldMap
  List(1, 2, 3).combineAll
  // res16: Int = 6
  List(1, 2, 3).foldMap(_.toString)
  // res17: String = 123

  
  }