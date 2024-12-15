package adventofcode.util.vector


// used when position is relative to start (and may become negative)
typealias  SparseGrid<T> = Map<Vec2, T>

fun SparseGrid<*>.xBounds() = keys.xBounds()
fun SparseGrid<*>.yBounds() = keys.yBounds()
fun Collection<Vec2>.xBounds() = (minOf { it.x })..(maxOf { it.x })
fun Collection<Vec2>.yBounds() = (minOf { it.y })..(maxOf { it.y })



//discards offset
fun <T> SparseGrid<T>.toGrid(default: T): List<List<T>> {
    val xs = xBounds()
    return yBounds().map { y ->
        xs.map { x ->
            getOrDefault(Vec2(x, y), default)
        }
    }
}
