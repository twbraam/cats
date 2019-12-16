package Chapter4_Monads

object Exercises {
  import cats.Id
  
  def pure[A](value: A): Id[A] =
    value
  def flatMap[A, B](initial: Id[A])(func: A => Id[B]): Id[B] =
    func(initial)
  def map[A, B](initial: Id[A])(func: A => B): Id[B] =
    func(initial)


  import cats.Eval
  def foldRightEval[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] =
    as match {
      case head :: tail =>
        Eval.defer(fn(head, foldRight(tail, acc)(fn)))
      case Nil =>
        acc
  }

  def foldRight[A, B](as: List[A], acc: B)(fn: (A, B) => B): B =
    foldRightEval(as, Eval.now(acc)) { (a, b) =>
      b.map(fn(a, _))
    }.value
}