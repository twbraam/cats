package Chapter2_Monoids

object MonoidInstances {
  implicit val booleanAndMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      def combine(a: Boolean, b: Boolean): Boolean = a && b
      def empty = true
    }

  implicit val booleanOrMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      def combine(a: Boolean, b: Boolean): Boolean = a || b
      def empty = false
    }

  implicit val booleanXorMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      def combine(a: Boolean, b: Boolean): Boolean = (a && !b) || (!a && b)
      def empty = false
    }

  implicit val booleanXnorMonoid: Monoid[Boolean] =
    new Monoid[Boolean] {
      def combine(a: Boolean, b: Boolean): Boolean =
        (!a || b) && (a || !b)
      def empty = true
    }
}
