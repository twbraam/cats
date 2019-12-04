package Chapter1

import PrintableInstances._


final case class Cat(name: String, age: Int, color: String)

object Cat {
  implicit val catPrinter: Printable[Cat] =
    new Printable[Cat] {
      def format(cat: Cat): String = {
        val name = Printable.format(cat.name)
        val age = Printable.format(cat.age)
        val color = Printable.format(cat.color)
        s"$name is a $age year-old $color cat."}
    }
}