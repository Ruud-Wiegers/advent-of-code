package adventofcode.util.collections

fun <T> Iterable<T>.cycle(): Sequence<T> = generateSequence { asSequence() }.flatten()