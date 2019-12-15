package adventofcode.util.vector

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

