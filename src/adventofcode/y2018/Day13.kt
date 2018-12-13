package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day13.solve()

object Day13 : AdventSolution(2018, 13, "Mine Cart Madness") {

    override fun solvePartOne(input: String): String {
        val (track, carts) = parse(input)


        while (true) {
            carts.sortBy { it.p.x }
            carts.sortBy { it.p.y }

            for (cart in carts) {
                cart.p += cart.d.vector

                if (carts.count { c -> cart.p == c.p } > 1) {
                    return "${cart.p.x},${cart.p.y}"
                }

                when (track[cart.p.y][cart.p.x]) {
                    '/' -> cart.d = when (cart.d) {
                        Direction.UP -> Direction.RIGHT
                        Direction.RIGHT -> Direction.UP
                        Direction.DOWN -> Direction.LEFT
                        Direction.LEFT -> Direction.DOWN
                    }

                    '\\' -> cart.d = when (cart.d) {
                        Direction.UP -> Direction.LEFT
                        Direction.LEFT -> Direction.UP
                        Direction.DOWN -> Direction.RIGHT
                        Direction.RIGHT -> Direction.DOWN
                    }
                    '+' -> {
                        when (cart.t) {
                            0 -> cart.d = cart.d.left()
                            2 -> cart.d = cart.d.right()
                        }
                        cart.t = (cart.t + 1) % 3
                    }
                }
            }
        }
    }

    override fun solvePartTwo(input: String): String {
        val (track, carts) = parse(input)

        while (true) {
            carts.sortBy { it.p.x }
            carts.sortBy { it.p.y }

            for (cart in carts) {
                cart.p += cart.d.vector

                val p = cart.p
                if (carts.count { c -> c.p == p } > 1) {
                    carts
                            .filter { it.p == p }
                            .forEach { it.p = Point(-1000000, -100) }
                }
                if (cart.p.x >= 0) {

                    when (track[cart.p.y][cart.p.x]) {
                        '/' -> cart.d = when (cart.d) {
                            Direction.UP -> Direction.RIGHT
                            Direction.RIGHT -> Direction.UP
                            Direction.DOWN -> Direction.LEFT
                            Direction.LEFT -> Direction.DOWN
                        }

                        '\\' -> cart.d = when (cart.d) {
                            Direction.UP -> Direction.LEFT
                            Direction.LEFT -> Direction.UP
                            Direction.DOWN -> Direction.RIGHT
                            Direction.RIGHT -> Direction.DOWN
                        }
                        '+' -> {
                            when (cart.t) {
                                0 -> cart.d = cart.d.left()
                                2 -> cart.d = cart.d.right()
                            }
                            cart.t = (cart.t + 1) % 3
                        }
                    }
                }
            }
            carts.removeIf { it.p.x < 0 }
            if (carts.size == 1) return "${carts[0].p.x},${carts[0].p.y}"


        }
    }

    private fun parse(input: String): Pair<List<List<Track>>, MutableList<Cart>> {
        val carts = mutableListOf<Cart>()
        val track = input.split("\n").mapIndexed { y, r ->
            r.mapIndexed { x, ch ->
                when (ch) {
                    '^' -> {
                        carts += Cart(Point(x, y), Direction.UP, 0);'|'
                    }
                    'V' -> {
                        carts += Cart(Point(x, y), Direction.DOWN, 0);'|'
                    }
                    '<' -> {
                        carts += Cart(Point(x, y), Direction.LEFT, 0);'-'
                    }
                    '>' -> {
                        carts += Cart(Point(x, y), Direction.RIGHT, 0);'-'
                    }
                    else -> ch
                }
            }
        }

        return track to carts
    }

    private data class Cart(var p: Point, var d: Direction, var t: Int)
}
private typealias Track = Char

private enum class Direction(val vector: Point) {
    UP(Point(0, -1)), RIGHT(Point(1, 0)), DOWN(Point(0, 1)), LEFT(Point(-1, 0));

    fun left() = turn(3)
    fun right() = turn(1)
    fun reverse() = turn(2)

    private fun turn(i: Int): Direction = Direction.values().let { it[(it.indexOf(this) + i) % it.size] }
}

private data class Point(val x: Int, val y: Int) {
    operator fun plus(o: Point) = Point(x + o.x, y + o.y)
}
