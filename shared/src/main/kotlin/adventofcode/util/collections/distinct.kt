package adventofcode.util.collections

/**
 * Take elements from a sequence until a repeated value occurs
 *
 * The operation is _intermediate_ and _stateful_.
 */
fun <T> Sequence<T>.takeWhileDistinct(): Sequence<T> {
    val history = mutableSetOf<T>()
    return takeWhile { it !in history }
        .onEach { history += it }
}

/**
 * drop elements from a sequence until a repeated value occurs, then return that element
 *
 * The operation is _terminal_.
 */
fun <T> Sequence<T>.firstDuplicate(): T? {
    val history = mutableSetOf<T>()
    return dropWhile { e -> (e !in history).also { history += e } }.firstOrNull()
}

/**
 * Discard consecutive duplicates
 *
 * The operation is _intermediate_ and _stateless_.
 */
fun <T> Sequence<T>.onlyChanges(): Sequence<T> = (this + sequenceOf(null)).zipWithNext { a, b -> a.takeUnless { a == b } }.filterNotNull()