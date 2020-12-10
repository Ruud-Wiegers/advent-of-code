package adventofcode.util.collections

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

fun <T, U> Sequence<T>.cartesian(other: Iterable<U>): Sequence<Pair<T, U>> =
    flatMap { a ->
        other.asSequence().map { b ->
            Pair(a, b)
        }
    }

/**
 * Generates combinations of distinct pairs of the source sequence, applying [transform]
 * TODO: iterates over the sequence multiple times, not very nice
 */
inline fun <T, R> List<T>.combinations(crossinline transform: (T, T) -> R): List<R> =
    flatMapIndexed { i, a ->
        take(i).map { b ->
            transform(a, b)
        }
    }