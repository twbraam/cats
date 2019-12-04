package Chapter1

object PrintableSyntax {
  implicit class PrintableOps[A](value: A) {
    //def format(implicit p: Printable[A]): String = p.format(value)
    def print(implicit p: Printable[A]): Unit = Printable.print(value)
  }
}
