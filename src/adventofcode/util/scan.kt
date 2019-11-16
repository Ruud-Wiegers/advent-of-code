package adventofcode.util

fun <T, R> Sequence<T>.scan(initial: R, operation: (R, T) -> R, includeInitial: Boolean = false): Sequence<R> {
    var result: R = initial
    val i = if (includeInitial) sequenceOf(result) else emptySequence()
    return i + this.map {
        result = operation(result, it)
        result
    }
}