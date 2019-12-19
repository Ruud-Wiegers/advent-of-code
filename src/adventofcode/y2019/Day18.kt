package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors
import java.util.*

fun main() {
    Day18.solve()
}

object Day18 : AdventSolution(2019, 18, "Many-Worlds Interpretation") {
    private val alphabet = 'a'..'z'

    override fun solvePartOne(input: String): Any? {
        val (floor, objectAtLocation) = readMaze(input)


        //TODO bitsets
        //TODO transitive closure on requirements
        //TODO key = also a door opened by itself

        val distancesWithClosedDoors: Map<Char, Map<Char, Int>> = generateDistanceMap(floor, objectAtLocation)
        val keyDistancesWithOpenDoors: Map<Char, Map<Char, Int>> = generateDistanceMap(floor + objectAtLocation.keys, objectAtLocation.filterValues { it in alphabet + '@' })

        val dependencies: Map<Char, Set<Char>> = buildDependencyTree(distancesWithClosedDoors)
        val keyRequirements = keyRequirements(dependencies)

        val keyRequiredBy = alphabet.associateWith { k -> alphabet.filter { k in keyRequirements[it]!! }.toSet() }

        var completion: Map<Char, Map<Set<Char>, Int>> = alphabet.associateWith { mapOf(setOf(it) to 0) }


        dependencies.forEach(::println)
        keyRequirements.forEach(::println)


        fun lookup(oldPos: Char): Map<Set<Char>, Int> {
            val required = keyRequirements[oldPos]!!
            val requiredBy = keyRequiredBy[oldPos]!!

            val startFrom = keyDistancesWithOpenDoors[oldPos]!!


            val new = mutableMapOf<Set<Char>, Int>()

            completion.map { (np, nv) ->
                nv.filter { (ks, _) -> oldPos !in ks && required.none { it in ks } && requiredBy.all { it in ks } }
                        .asSequence()
                        .map { (ks, cost) ->
                            (ks + oldPos) to (startFrom[np]!! + cost)
                        }
                        .filter { (k, v) -> new[k]?.let {  it > v}?:true }
                        .forEach { (k, v) -> new[k] = v }
            }

            return new
        }


        repeat(alphabet.last - alphabet.first ) {
            println(it)

            completion = alphabet.associateWith { oldPos -> lookup(oldPos) }

        }
         return completion
                 .filter { it.key in dependencies['@']!! }
                 .mapValues { keyDistancesWithOpenDoors['@']!![it.key]!! + it.value.values.first() }
                 .values
                 .min()
    }

    private fun keyRequirements(dependencies: Map<Char, Set<Char>>): Map<Char, SortedSet<Char>> {
        fun Map<Char, Set<Char>>.find(current: Char, target: Char): List<Char>? = when {
            target in this[current]!! -> listOf(current)
            else                      -> this[current]!!
                    .filter { it in alphabet.map { it.toUpperCase() } }
                    .mapNotNull { this.find(it, target) }
                    .firstOrNull()
                    ?.let { it + current }
        }

        return alphabet.associateWith { dependencies.find('@', it)!!.let { it.map { it.toLowerCase() } - '@' }.toSortedSet() }
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
        floor += objectAtLocation.filterValues { it !in alphabet.map { it.toUpperCase() } }.keys
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
                new.filter { it in alphabet.map { it.toUpperCase() } }
            }.toSet()
        }
        return tree.toSortedMap()
    }


    override fun solvePartTwo(input: String): String = "todo"
}
