package adventofcode.util

/**
 * Generates a the cartesian product of a list with itself, as a sequence of pairs
 */
fun <T> List<T>.cartesian() = this.cartesian(this)

/**
 * Generates a the cartesian product of a list with another list, as a sequence of pairs
 */
fun <T, U> List<T>.cartesian(other: List<U>): Sequence<Pair<T, U>> =
        asSequence().flatMap { a ->
            other.asSequence().map { b ->
                Pair(a, b)
            }
        }