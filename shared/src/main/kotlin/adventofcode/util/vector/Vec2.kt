package adventofcode.util.vector

import kotlin.math.absoluteValue

data class Vec2(val x: Int, val y: Int) {
    operator fun plus(o: Vec2) = Vec2(x + o.x, y + o.y)
    operator fun minus(o: Vec2) = Vec2(x - o.x, y - o.y)
    operator fun div(o: Int) = Vec2(x / o, y / o)
    operator fun times(o: Int) = Vec2(x * o, y * o)
    fun distance(o: Vec2) = (o - this).let { it.x.absoluteValue + it.y.absoluteValue }

    companion object {
        val origin = Vec2(0, 0)
    }
}