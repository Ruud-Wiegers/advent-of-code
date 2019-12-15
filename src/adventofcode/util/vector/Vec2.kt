package adventofcode.util.vector

data class Vec2(val x: Int, val y: Int) {
    operator fun plus(o: Vec2) = Vec2(x + o.x, y + o.y)
    operator fun minus(o: Vec2) = Vec2(x - o.x, y - o.y)
    operator fun div(o: Int) = Vec2(x / o, y / o)

    companion object {
        val origin = Vec2(0, 0)
    }
}