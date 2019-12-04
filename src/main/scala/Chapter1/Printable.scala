package Chapter1

trait Printable[A] {
  def format(text: A): String
}

object Printable {
  def format[A](text: A)(implicit p: Printable[A]): String = p.format(text)
  def print[A](text: A)(implicit p: Printable[A]): Unit = println(format(text))
}