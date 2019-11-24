package adventofcode.util

/**
 * Generates a the cartesian product of a list with itself, as a sequence of pairs
 */
fun <T> Iterable<T>.cartesian() = this.cartesian(this)

/**
 * Generates a the cartesian product of a list with another list, as a sequence of pairs
 */
fun <T, U> Iterable<T>.cartesian(other: Iterable<U>): Sequence<Pair<T, U>> =
        asSequence().flatMap { a ->
            other.asSequence().map { b ->
                Pair(a, b)
            }
        }