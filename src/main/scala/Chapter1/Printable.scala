package Chapter1

trait Printable[A] {
  def format(text: A): String
}

object Printable {
  def format[A](value: A)(implicit valuePrinter: Printable[A]) = valuePrinter.format(value)
  def print[A](value: A)(implicit valuePrinter: Printable[A]) = println(valuePrinter.format(value))
}