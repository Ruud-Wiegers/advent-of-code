package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() {
    Day20.solve()
}

object Day20 : AdventSolution(2019, 20, "Donut maze") {
    override fun solvePartOne(input: String): Any? {
        val (floor, labels) = readMaze(input)

        val portals = labelPortals(floor, labels, Direction.RIGHT) + labelPortals(floor, labels, Direction.DOWN)

        val portalNeighbors = portals
                .asSequence()
                .mapNotNull { (entrance, entranceName) ->
                    portals.asSequence()
                            .find { (exit, exitName) -> entrance != exit && entranceName == exitName }
                            ?.key
                            ?.let { entrance to it }
                }
                .toMap()

        val vs = Direction.values().map { it.vector }
        fun neighbors(p: Vec2) = vs.map { it + p }
                .filter { it in floor } + portalNeighbors[p]?.let { listOf(it) }.orEmpty()


        val start = portals.asSequence().first { it.value == "AA" }.key
        val goal = portals.asSequence().first { it.value == "ZZ" }.key


        var open = listOf(start)
        val closed = mutableSetOf<Vec2>()
        var count = 0
        while (open.isNotEmpty()) {
            count++
            closed += open
            open = open.flatMap { neighbors(it) }.filter { it !in closed }
            if (goal in open) return count
        }

        return "failed"
    }

    private fun labelPortals(floor: Set<Vec2>, labels: Map<Vec2, Char>, direction: Direction): Map<Vec2, String> = labels
            .filterKeys { k -> k + direction.vector in labels }
            .mapValues { (k, v) -> v.toString() + labels.getValue(k + direction.vector) }
            .mapKeys { it.key + if (it.key + direction.reverse.vector in floor) direction.reverse.vector else direction.vector * 2 }

    private fun readMaze(input: String): Pair<Set<Vec2>, Map<Vec2, Char>> {
        val floor = mutableSetOf<Vec2>()
        val objectAtLocation = mutableMapOf<Vec2, Char>()
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                when (ch) {
                    '.' -> floor += Vec2(x, y)
                    ' ', '#' -> {
                    }
                    else -> objectAtLocation[Vec2(x, y)] = ch
                }
            }
        }
        return Pair(floor, objectAtLocation)
    }


    override fun solvePartTwo(input: String): Int {
        val (floor, labels) = readMaze(input)

        val portals = labelPortals(floor, labels, Direction.RIGHT) + labelPortals(floor, labels, Direction.DOWN)


        fun Vec2.isInside() = x == 34 || y == 34 || x == 92 || y == 92

        val links = portals.asSequence()
                .mapNotNull { (entrance, entranceName) ->
                    portals.asSequence()
                            .find { (exit, exitName) -> entrance != exit && entranceName == exitName }
                            ?.key?.let { entrance to it }
                }
                .toMap()
                .mapValues { (entrance, exit) -> if (entrance.isInside()) Pair(exit, 1) else Pair(exit, -1) }

        links.forEach(::println)

        fun portalNeighbors(p: Vec2, level: Int) = links[p]?.let { listOf(it.first to it.second + level) }.orEmpty()
                .filter { it.second >= 0 }
        val vs = Direction.values().map { it.vector }
        fun floorNeighbors(p: Vec2, level: Int) = vs.map { (it + p) to level }.filter { it.first in floor }
        fun neighbors(p: Vec2, level: Int) = portalNeighbors(p, level) + floorNeighbors(p, level)


        val start = portals.asSequence().first { it.value == "AA" }.key to 0
        val goal = portals.asSequence().first { it.value == "ZZ" }.key to 0


        var open = listOf(start)
        val closed = mutableSetOf<Pair<Vec2, Int>>()
        var count = 0
        while (open.isNotEmpty()) {
            count++
            closed += open
            open = open.flatMap { neighbors(it.first, it.second) }.filter { it !in closed }
            if (goal in open) return count
        }

        return -1
    }
}
