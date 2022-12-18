package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec3


object Day18 : AdventSolution(2022, 18, "Boiling Boulders") {

    override fun solvePartOne(input: String): Int {
        val lava = parse(input).toList()

        val internalFaces = lava.flatMap { l -> directions.map(l::plus) }.count(lava::contains)

        return (6 * lava.size) - internalFaces
    }

    override fun solvePartTwo(input: String): Int {
        val lava = parse(input).toSet()

        //bounding box
        val xs = lava.minOf { it.x } - 1..lava.maxOf { it.x } + 1
        val ys = lava.minOf { it.y } - 1..lava.maxOf { it.y } + 1
        val zs = lava.minOf { it.z } - 1..lava.maxOf { it.z } + 1

        //all reachable cubes
        fun bfs(start: Vec3, reachableNeighbors: (Vec3) -> List<Vec3>): Set<Vec3> =
            generateSequence(Pair(setOf(start), setOf(start))) { (frontier, visited) ->
                val unexploredNeighbors = frontier.flatMap(reachableNeighbors).toSet() - visited
                Pair(unexploredNeighbors, visited + unexploredNeighbors)
            }
                .takeWhile { (frontier, _) -> frontier.isNotEmpty() }
                .last().second

        //flood-fill the outside
        val casing = bfs(Vec3.origin) { v ->

            //reachable if in box but not in lava
            directions.map(v::plus)
                .filterNot(lava::contains)
                .filter { it.x in xs && it.y in ys && it.z in zs }
        }

        //all faces that touch the casing are exposed
        return lava.flatMap { l -> directions.map(l::plus) }.count(casing::contains)
    }

    private fun parse(input: String) =
        input.lineSequence()
            .map { it.split(',').map(String::toInt) }
            .map { Vec3(it[0], it[1], it[2]) }

    private val directions = listOf(
        Vec3(-1, 0, 0),
        Vec3(1, 0, 0),
        Vec3(0, -1, 0),
        Vec3(0, 1, 0),
        Vec3(0, 0, -1),
        Vec3(0, 0, 1)
    )
}