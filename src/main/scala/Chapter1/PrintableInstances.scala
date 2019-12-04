package Chapter1

object PrintableInstances {
  implicit val stringPrinter: Printable[String] =
    new Printable[String] {
      def format(value: String): String = value
    }

  implicit val intPrinter: Printable[Int] =
    new Printable[Int] {
      def format(value: Int): String = value.toString
    }

  implicit val catPrinter = new Printable[Cat] {
    def format(cat: Cat) = {
    val name = Printable.format(cat.name)
    val age = Printable.format(cat.age)
    val color = Printable.format(cat.color)
    s"$name is a $age year-old $color cat."
    }
  }
}