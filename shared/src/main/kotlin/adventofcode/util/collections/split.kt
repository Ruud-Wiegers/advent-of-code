package adventofcode.util.collections


/**
 * Splits this collection into multiple non-empty sublists. A split is made where [condition] on two consecutive elements is true
 */
fun <T> Iterable<T>.splitWhen(condition: (a: T, b: T) -> Boolean): List<List<T>> {
    val iterator = iterator()
    if (!iterator.hasNext()) return emptyList()

    val result = mutableListOf<List<T>>()
    var sublist = mutableListOf<T>()

    var current = iterator.next()
    while (iterator.hasNext()) {
        val next = iterator.next()
        sublist.add(current)
        if (condition(current, next)) {
            result += sublist
            sublist = mutableListOf()
        }
        current = next
    }

    sublist.add(current)
    result += sublist
    return result
}

fun <T> Iterable<T>.splitBefore(condition: (a: T) -> Boolean): List<List<T>> = splitWhen { _, b -> condition(b) }
fun <T> Iterable<T>.splitAfter(condition: (a: T) -> Boolean): List<List<T>> = splitWhen { a, _ -> condition(a) }
