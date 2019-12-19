package Chapter7_Foldable

object FoldExercises {
  def foldMap[A, B](input: List[A])(f: A => B): List[B] = 
  input.foldRight(List.empty[B])((x, acc) => f(x) :: acc)

  def foldFlatMap[A, B](input: List[A])(f: A => List[B]): List[B] = 
    input.foldRight(List.empty[B])((x, acc) => f(x) ::: acc)

  def foldFilter[A](input: List[A])(f: A => Boolean): List[A] = 
    input.foldRight(List.empty[A])((x, acc) => if (f(x)) x  :: acc else acc)
}