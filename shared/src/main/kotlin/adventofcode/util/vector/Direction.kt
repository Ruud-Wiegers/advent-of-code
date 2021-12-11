package adventofcode.util.vector

import adventofcode.util.collections.cartesian

enum class Direction(val vector: Vec2) {
    UP(Vec2(0, -1)), RIGHT(Vec2(1, 0)), DOWN(Vec2(0, 1)), LEFT(Vec2(-1, 0));

    val reverse by lazy {
        when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }

    val turnLeft by lazy {
        when (this) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> DOWN
            RIGHT -> UP
        }
    }

    val turnRight by lazy {
        when (this) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> UP
            RIGHT -> DOWN
        }
    }

}

fun Vec2.neighbors(): List<Vec2> = Direction.values().map { this + it.vector }

val mooreDelta = (-1..1).cartesian().map { (dx, dy) -> Vec2(dx, dy) }.toList()
fun Vec2.mooreNeighbors(): List<Vec2> = mooreDelta.map(this::plus)