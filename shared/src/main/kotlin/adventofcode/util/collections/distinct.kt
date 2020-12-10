package adventofcode.util.collections

fun <T> Sequence<T>.takeWhileDistinct(): Sequence<T> {
    val history = mutableSetOf<T>()
    return takeWhile { it !in history }
        .onEach { history += it }
}
