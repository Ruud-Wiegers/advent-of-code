package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import java.lang.IllegalStateException

fun main() {
    Day18.solve()
}

object Day18 : AdventSolution(2019, 18, "Many-Worlds Interpretation") {
    private val alphabet = 'a'..'z'

    override fun solvePartOne(input: String): Int? {
        val (floor, objectAtLocation) = readMaze(input)
        val distancesWithClosedDoors: Map<Char, Map<Char, Int>> = generateDistanceMap(floor, objectAtLocation)
        val keyDistancesWithOpenDoors: Map<Char, Map<Char, Int>> = generateDistanceMap(floor + objectAtLocation.keys, objectAtLocation.filterValues { it in alphabet + '@' })

        val keysNeededFor: Map<Char, Set<Char>> = requiredKeysForKey(distancesWithClosedDoors, listOf('@'))

        return memoizedPath(keysNeededFor, keyDistancesWithOpenDoors)

    }

    private fun memoizedPath(keysNeededFor: Map<Char, Set<Char>>, keyDistancesWithOpenDoors: Map<Char, Map<Char, Int>>): Int? {
        val bestPaths = mutableMapOf<Pair<Char, Set<Char>>, Int?>()

        fun bestPaths(from: Char, collected: Set<Char>): Int? = bestPaths.getOrPut(from to collected) {
            if (collected.size > 25) 0
            else alphabet.asSequence()
                    .filter { it !in collected }
                    .filter { keysNeededFor[it].orEmpty().all { it in collected } }
                    .mapNotNull { to -> bestPaths(to, collected + to)?.let { it + keyDistancesWithOpenDoors[from]!![to]!! } }
                    .min()

        }
        return bestPaths('@', emptySet())
    }

    override fun solvePartTwo(input: String): Any? {
        val (floor, objectAtLocation) = readMaze(input)
        val center = objectAtLocation.asSequence().first { it.value == '@' }.key
        floor.apply {
            remove(center)
            Direction.values().map { it.vector + center }.forEach { remove(it) }
        }
        objectAtLocation.remove(center)
        objectAtLocation[center + Vec2(-1, -1)] = '1'
        objectAtLocation[center + Vec2(1, -1)] = '2'
        objectAtLocation[center + Vec2(-1, 1)] = '3'
        objectAtLocation[center + Vec2(1, 1)] = '4'

        val distancesWithClosedDoors: Map<Char, Map<Char, Int>> = generateDistanceMap(floor, objectAtLocation)
        val keyDistancesWithOpenDoors: Map<Char, Map<Char, Int>> = generateDistanceMap(floor + objectAtLocation.keys, objectAtLocation.filterValues { it in alphabet || it in '1'..'4' })

        val keysNeededFor: Map<Char, Set<Char>> = requiredKeysForKey(distancesWithClosedDoors, "1234".toList())


        val bestPaths = mutableMapOf<Pair<Set<Char>, Set<Char>>, Int?>()

        fun bestPaths(from: Set<Char>, collected: Set<Char>): Int? = bestPaths.getOrPut(from to collected) {
            if (collected.size == alphabet.last - alphabet.first + 1) 0
            else alphabet.asSequence()
                    .filter { it !in collected }
                    .filter { keysNeededFor[it].orEmpty().all { it in collected } }
                    .mapNotNull { to ->

                        val toRemove = from.intersect(keyDistancesWithOpenDoors[to]!!.keys).single()

                        bestPaths(from - toRemove + to, collected + to)?.let { it + keyDistancesWithOpenDoors[toRemove]!![to]!! }
                    }
                    .min()
        }


        return bestPaths("1234".toSet(), emptySet())
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
        return Pair(floor, objectAtLocation)
    }

    private fun generateDistanceMap(floor: Set<Vec2>, objectAtLocation: Map<Vec2, Char>): Map<Char, Map<Char, Int>> {
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

        return objectAtLocation.asSequence().associate { it.value to findOpenPaths(it.value, it.key) }
    }

    private fun requiredKeysForKey(directDistances: Map<Char, Map<Char, Int>>, start: List<Char>): Map<Char, Set<Char>> {
        val requiredKeysForKey = start.associateWith { emptySet<Char>() }.toMutableMap()

        var open = start.toSet()

        while (open.isNotEmpty()) {
            val new = mutableSetOf<Char>()
            open.forEach { from ->
                directDistances[from]?.keys.orEmpty()
                        .filter { it !in requiredKeysForKey.keys }
                        .forEach { to ->
                            requiredKeysForKey[to] = requiredKeysForKey[from].orEmpty() + from.toLowerCase()
                            new += to
                        }
            }
            open = new
        }
        return requiredKeysForKey.mapValues { it.value.filter { it in alphabet }.toSet() }.filterKeys { it in alphabet }
    }
}
