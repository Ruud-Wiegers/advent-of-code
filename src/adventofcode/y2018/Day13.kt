package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.y2018.Day13.Direction.*

fun main() = Day13.solve()

object Day13 : AdventSolution(2018, 13, "Mine Cart Madness") {

    override fun solvePartOne(input: String): String {
        val (track, carts) = parse(input)

        while (true) {
            for (cart in carts.sortedBy { it.p.y * 10000 + it.p.x }) {
                cart.move(track)

                if (carts.count { it.p == cart.p } > 1)
                    return cart.position()
            }
        }
    }

    override fun solvePartTwo(input: String): String {
        val (track, carts) = parse(input)

        while (true) {
            for (cart in carts.sortedBy { it.p.y * 10000 + it.p.x }) {
                cart.move(track)

                if (carts.count { it.p == cart.p } > 1)
                    carts.removeIf { it.p == cart.p }
            }

            if (carts.size == 1)
                return carts[0].position()
        }
    }

    private fun Cart.move(track: List<String>) {
        if (p.x < 0 || p.y < 0) return

        p += d.vector

        when (track[p.y][p.x]) {
            '/' -> d = when (d) {
                UP -> RIGHT
                RIGHT -> UP
                DOWN -> LEFT
                LEFT -> DOWN
            }

            '\\' -> d = when (d) {
                UP -> LEFT
                LEFT -> UP
                DOWN -> RIGHT
                RIGHT -> DOWN
            }
            '+' -> {
                when (t) {
                    0 -> d = d.left()
                    2 -> d = d.right()
                }
                t = (t + 1) % 3
            }
        }
    }

    private fun parse(input: String): Pair<List<String>, MutableList<Cart>> {
        val track = input.split("\n")

        val carts = mutableListOf<Cart>()
        track.forEachIndexed { y, r ->
            r.forEachIndexed { x, ch ->
                when (ch) {
                    '^' -> carts += Cart(Point(x, y), UP, 0)
                    'v' -> carts += Cart(Point(x, y), DOWN, 0)
                    '<' -> carts += Cart(Point(x, y), LEFT, 0)
                    '>' -> carts += Cart(Point(x, y), RIGHT, 0)
                }
            }
        }

        return track to carts
    }

    private data class Cart(var p: Point, var d: Direction, var t: Int) {
        fun position() = "${p.x},${p.y}"
    }

    private enum class Direction(val vector: Point) {
        UP(Point(0, -1)), RIGHT(Point(1, 0)), DOWN(Point(0, 1)), LEFT(Point(-1, 0));

        fun left() = turn(3)
        fun right() = turn(1)

        private fun turn(i: Int): Direction = values().let { it[(it.indexOf(this) + i) % it.size] }
    }

    private data class Point(val x: Int, val y: Int) {
        operator fun plus(o: Point) = Point(x + o.x, y + o.y)
    }
}