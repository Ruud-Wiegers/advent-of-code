package adventofcode.util

fun <T, R> Sequence<T>.scan(initial: R, operation: (R, T) -> R): Sequence<R> {
    var result: R = initial
    return this.map {
        result = operation(result, it)
        result
    }
}
