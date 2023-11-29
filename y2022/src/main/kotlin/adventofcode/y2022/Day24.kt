package adventofcode.y2022

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

object Day24 : AdventSolution(2022, 24, "Blizzard Basin") {

    override fun solvePartOne(input: String): Int {
        val initialBlizzards: List<Blizzard> = parse(input)

        val xs = initialBlizzards.minOf { it.p.x }..initialBlizzards.maxOf { it.p.x }
        val ys = initialBlizzards.minOf { it.p.y }..initialBlizzards.maxOf { it.p.y }


        fun Vec2.wrap(xs: IntRange, ys: IntRange) = Vec2(
            when {
                x < xs.first -> xs.last
                x > xs.last -> xs.first
                else -> x
            },
            when {
                y < ys.first -> ys.last
                y > ys.last -> ys.first
                else -> y
            }
        )


        val blizzards = generateSequence(initialBlizzards) {
            it.map {
                it.step().let { it.copy(p = it.p.wrap(xs, ys)) }
            }
        }.map { it.map { it.p }.toSet() }.drop(1)

        val start = Vec2(1, 0)
        val target = Vec2(xs.last, ys.last + 1)

        val wallXs = xs.first - 1..xs.last + 1
        val wallYs = ys.first - 1..ys.last + 1

        val walls = buildSet {
            addAll(wallXs.map { Vec2(it, wallYs.first) })

            addAll(wallXs.map { Vec2(it, wallYs.last) })
            addAll(wallYs.map { Vec2(wallXs.first, it) })
            addAll(wallYs.map { Vec2(wallXs.last, it) })
            add(Vec2(wallXs.first + 1, wallYs.first - 1))
            add(Vec2(wallXs.last - 1, wallYs.last + 1))
            remove(start)
            remove(target)
        }


        return blizzards.scan(setOf(start)) { frontier, currentBlizzards ->
            val unexploredNeighbors = frontier.flatMap { it.neighbors() + it }.toSet()
            unexploredNeighbors - walls - currentBlizzards
        }
            .indexOfFirst { target in it }


    }


    override fun solvePartTwo(input: String): Int {
        val initialBlizzards: List<Blizzard> = parse(input)

        val xs = initialBlizzards.minOf { it.p.x }..initialBlizzards.maxOf { it.p.x }
        val ys = initialBlizzards.minOf { it.p.y }..initialBlizzards.maxOf { it.p.y }


        fun Vec2.wrap(xs: IntRange, ys: IntRange) = Vec2(
            when {
                x < xs.first -> xs.last
                x > xs.last -> xs.first
                else -> x
            },
            when {
                y < ys.first -> ys.last
                y > ys.last -> ys.first
                else -> y
            }
        )


        val blizzards = generateSequence(initialBlizzards) {
            it.map {
                it.step().let { it.copy(p = it.p.wrap(xs, ys)) }
            }
        }.map { it.map { it.p }.toSet() }.drop(1)

        val start = Vec2(1, 0) to 0
        val target = Vec2(xs.last, ys.last + 1) to 2

        val wallXs = xs.first - 1..xs.last + 1
        val wallYs = ys.first - 1..ys.last + 1

        val walls = buildSet {
            addAll(wallXs.map { Vec2(it, wallYs.first) })

            addAll(wallXs.map { Vec2(it, wallYs.last) })
            addAll(wallYs.map { Vec2(wallXs.first, it) })
            addAll(wallYs.map { Vec2(wallXs.last, it) })
            add(Vec2(wallXs.first + 1, wallYs.first - 1))
            add(Vec2(wallXs.last - 1, wallYs.last + 1))
            remove(start.first)
            remove(target.first)
        }


        return blizzards.scan(setOf(start)) { frontier, currentBlizzards ->
            frontier
                .asSequence()
                .flatMap { (p, leg) ->
                    (p.neighbors() + p).map { new ->
                        new to when {
                            leg == 0 && new == target.first -> 1
                            leg == 1 && new == start.first -> 2
                            else -> leg
                        }
                    }
                }
                .filterNot { it.first in walls }
                .filterNot { it.first in currentBlizzards }
                .toSet()

        }
            .indexOfFirst { target in it }


    }

    private fun parse(input: String) = buildList {
        input.lineSequence().forEachIndexed { y, row ->
            row.forEachIndexed { x, ch ->
                when (ch) {
                    '<' -> add(Blizzard(Vec2(x, y), Direction.LEFT))
                    '>' -> add(Blizzard(Vec2(x, y), Direction.RIGHT))
                    '^' -> add(Blizzard(Vec2(x, y), Direction.UP))
                    'v' -> add(Blizzard(Vec2(x, y), Direction.DOWN))
                }
            }
        }
    }


    private data class Blizzard(val p: Vec2, val d: Direction) {
        fun step() = copy(p = p + d.vector)
    }

}