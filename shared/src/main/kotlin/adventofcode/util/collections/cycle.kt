package adventofcode.util.collections


/**
 * Generate a sequence of repeating values from a source iterable
 *
 * The operation is _intermediate_ and _stateless_.
 */
fun <T> Iterable<T>.cycle(): Sequence<T> = generateSequence { asSequence() }.flatten()