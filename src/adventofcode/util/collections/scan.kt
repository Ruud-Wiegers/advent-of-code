package adventofcode.util.collections

inline fun <T, R> Sequence<T>.scan(initial: R, crossinline operation: (R, T) -> R): Sequence<R> {
    var result: R = initial
    return this.map {
        result = operation(result, it)
        result
    }
}
