package adventofcode.util.vector


// used when position is relative to start (and may become negative)
typealias  SparseGrid<T> = Map<Vec2, T>

fun SparseGrid<*>.yBounds() = (keys.minOf { it.y })..(keys.maxOf { it.y })

fun SparseGrid<*>.xBounds(): IntRange = (keys.minOf { it.x })..(keys.maxOf { it.x })

//discards offset
fun <T> SparseGrid<T>.toGrid(default: T): List<List<T>> {
    val xs = xBounds()
    return yBounds().map { y ->
        xs.map { x ->
            getOrDefault(Vec2(x, y), default)
        }
    }
}
