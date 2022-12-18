package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.collections.cycle
import adventofcode.util.collections.takeWhileDistinct
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.Vec3


object Day18 : AdventSolution(2022, 18, "Boiling Boulders") {

    override fun solvePartOne(input: String): Int {
        val parsed = parse(input).toList()

        val neighbors = parsed.flatMap { directions.map { d -> it + d } }.count { it in parsed }

        return (6 * parsed.size) - neighbors
    }

    override fun solvePartTwo(input: String): Int {
        val parsed = parse(input).toSet()

        val xs = parsed.minOf { it.x } - 1..parsed.maxOf { it.x } + 1
        val ys = parsed.minOf { it.y } - 1..parsed.maxOf { it.y } + 1
        val zs = parsed.minOf { it.z } - 1..parsed.maxOf { it.z } + 1



        fun bfs(start: Vec3, reachableNeighbors: (Vec3) -> List<Vec3>): Set<Vec3> =
            generateSequence(Pair(setOf(start), setOf(start))) { (frontier, visited) ->
                val unexploredNeighbors = frontier.flatMap(reachableNeighbors).toSet() - visited
                Pair(unexploredNeighbors, visited + unexploredNeighbors)
            }
                .takeWhile { (frontier, _) -> frontier.isNotEmpty() }
                .last().second

        val casing = bfs(Vec3.origin) { v->
            directions.map { d->d+v }
                .filter { it.x in xs && it.y in ys &&it.z in zs }
                .filter { it !in parsed }
        }

        return parsed.flatMap { directions.map { d -> it + d } }.count { it in casing }


    }

    private fun parse(input: String) =
        input.lineSequence().map { it.split(',').map { it.toInt() } }.map { Vec3(it[0], it[1], it[2]) }

    private val directions = listOf(
        Vec3(-1, 0, 0),
        Vec3(1, 0, 0),
        Vec3(0, -1, 0),
        Vec3(0, 1, 0),
        Vec3(0, 0, -1),
        Vec3(0, 0, 1)
    )
}