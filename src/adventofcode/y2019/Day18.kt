package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import java.util.*

fun main() = Day18.solve()

object Day18 : AdventSolution(2019, 18, "Many-Worlds Interpretation") {

    override fun solvePartOne(input: String): Int? {
        val (floor, objectAtLocation) = readMaze(input)

        val distancesWithClosedDoors: Map<Char, Map<Char, Int>> = generateDistanceMap(floor, objectAtLocation)
        val keyDistancesWithOpenDoors: Map<Char, Map<Char, Int>> = generateDistanceMap(floor + objectAtLocation.keys, objectAtLocation.filterValues { it in ('a'..'z') + '@' })

        val dependencies: Map<Char, Set<Char>> = buildDependencyTree(distancesWithClosedDoors)
        val keyRequirements = keyRequirements(dependencies)

        val completion = (('a'..'z')+ '@').associateWith { mutableMapOf(emptySet<Char>() to 0) }

        fun dp(pos: Char, missingKeys: Set<Char>): Int =
                completion.getValue(pos).getOrPut(missingKeys) {
                    missingKeys.asSequence().filter { keyRequirements[it]!!.none { it in missingKeys } }
                            .map {
                                val c = dp(it, missingKeys - it)
                                c + keyDistancesWithOpenDoors.getValue(pos).getValue(it)
                            }
                            .min() ?: throw IllegalStateException()
                }

        return dp('@', ('a'..'z').toSet())


    }

    private fun keyRequirements(dependencies: Map<Char, Set<Char>>): Map<Char, SortedSet<Char>> {
        fun Map<Char, Set<Char>>.find(current: Char, target: Char): List<Char>? = when {
            target in this[current]!! -> listOf()
            else                      -> this[current]!!
                    .filter { it in 'A'..'Z' }
                    .mapNotNull { this.find(it, target) }
                    .firstOrNull()
                    ?.let { it + current }
        }

        val keyRequirements = ('a'..'z').associateWith { dependencies.find('@', it)!!.let { it - '@' }.toSortedSet() }
        return keyRequirements
    }


    private fun pathfinder(distances: Map<Char, Map<Char, Int>>) {

    }

    private fun readMaze(input: String): Pair<MutableSet<Vec2>, MutableMap<Vec2, Char>> {
        val floor = mutableSetOf<Vec2>()
        val objectAtLocation = mutableMapOf<Vec2, Char>()
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                when (ch) {
                    '.'  -> floor += Vec2(x, y)
                    '#'  -> {
                    }
                    else -> objectAtLocation[Vec2(x, y)] = ch
                }
            }
        }
        floor += objectAtLocation.filterValues { it !in 'A'..'Z' }.keys
        return Pair(floor, objectAtLocation)
    }

    private fun generateDistanceMap(floor: Set<Vec2>, objectAtLocation: Map<Vec2, Char>): Map<Char, Map<Char, Int>> {
        val distances = mutableMapOf<Char, Map<Char, Int>>()
        val neighbors = Direction.values().map { it.vector }
        fun Vec2.openNeighbors() = neighbors.map { this + it }.filter { it in floor }

        fun findOpenPaths(startObj: Char, start: Vec2): Map<Char, Int> {
            var open = setOf(start)
            val closed = mutableSetOf(start)
            val objects = mutableMapOf(startObj to 0)

            var distance = 0
            while (open.isNotEmpty()) {
                distance++
                open = open.flatMap { it.openNeighbors() }.filter { it !in closed }.toSet()
                closed += open
                open.forEach { target ->
                    target.neighbors().mapNotNull { objectAtLocation[it] }
                            .forEach { objects.putIfAbsent(it, distance + 1) }
                }
            }
            return objects.toSortedMap()
        }

        objectAtLocation.forEach { distances[it.value] = findOpenPaths(it.value, it.key) }

        return distances.toSortedMap()
    }

    private fun buildDependencyTree(distances: Map<Char, Map<Char, Int>>): Map<Char, Set<Char>> {
        val tree = mutableMapOf<Char, Set<Char>>()

        val visited = mutableSetOf('@')
        var open = setOf('@')

        while (open.isNotEmpty()) {
            open = open.flatMap {
                val new = distances[it]!!.keys.filter { it !in visited }.toSet()
                tree += it to new
                visited += new
                new.filter { it in 'A'..'Z' }
            }.toSet()
        }
        return tree.toSortedMap()
    }


    override fun solvePartTwo(input: String): String = "todo"
}
