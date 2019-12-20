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

        val dependencies: Map<Char, Set<Char>> = buildDependencyTree(distancesWithClosedDoors, listOf('@'))

        val keysNeededFor: Map<Char, Set<Char>> = keyRequirements(dependencies, listOf('@'))
        val keysThatNeed = alphabet.associateWith { k -> alphabet.filter { k in keysNeededFor[it].orEmpty() }.toSet() }

        val completion: Map<Char, Map<Set<Char>, Int>> = dijkstraStylePathbuilding(keysThatNeed, keyDistancesWithOpenDoors)


        return completion['@']?.values?.min()
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

        val dependencies: Map<Char, Set<Char>> = buildDependencyTree(distancesWithClosedDoors, ('1'..'4').toList())

        val keysNeededFor: Map<Char, Set<Char>> = keyRequirements(dependencies, ('1'..'4').toList())

        val keysThatNeed = alphabet.associateWith { k -> alphabet.filter { k in keysNeededFor[it].orEmpty() }.toSet() }

        val result = dijkstraStylePathbuildingV2(keysNeededFor, keysThatNeed, keyDistancesWithOpenDoors)


        val magic = result.map { it.key.positions to it.key.positions.sumBy { keyDistancesWithOpenDoors[it]!!.asSequence().single { it.key in "1234" }.value } + it.value }


        keysNeededFor.forEach(::println)
        keysThatNeed.forEach(::println)
        magic.forEach { println("${it.first}: ${it.second}") }

        return result
    }

    private data class State(val positions: Set<Char>, val keys: Set<Char>)

    private fun dijkstraStylePathbuildingV2(
            keysNeededFor: Map<Char, Set<Char>>,
            keysThatNeed: Map<Char, Set<Char>>,
            keyDistancesWithOpenDoors: Map<Char, Map<Char, Int>>
    ): Map<State, Int> {


        val starts = ('1'..'4').map { (keyDistancesWithOpenDoors[it]!!.keys - it) }

        val candidates = sequence {
            for (a in starts[0])
                for (b in starts[1])
                    for (c in starts[2])
                        for (d in starts[3])
                            yield(setOf(a, b, c, d))
        }.toList()


        fun Map<State, Int>.lookup(oldPos: Char): Sequence<Pair<State, Int>> {

            val keysNeeded = keysNeededFor[oldPos].orEmpty()
            val keysNeeding = keysThatNeed[oldPos].orEmpty()
            val startFrom = keyDistancesWithOpenDoors[oldPos].orEmpty()

            return this
                    .asSequence()
                    .filter { (newState) -> oldPos !in newState.keys && keysNeeded.none { it in newState.keys } && keysNeeding.all { it in newState.keys } }
                    .map { (newState, cost) ->
                        val (posToReplace, distance) = newState.positions.map { it to startFrom[it] }.single { it.second != null }
                        State(newState.positions - posToReplace + oldPos, newState.keys + oldPos) to distance!! + cost
                    }
        }

        var distances: Map<State, Int> = candidates.map { State(it, it) }.associateWith { 0 }


        repeat(22)
        {
            val new: MutableMap<State, Int> = mutableMapOf()
            ( alphabet.asSequence()).flatMap { oldPos -> distances.lookup(oldPos) }.forEach { (k, v) ->
                new.merge(k, v, ::minOf)
            }
            distances = new
        }

        return distances
    }


    private fun dijkstraStylePathbuilding(keysThatNeed: Map<Char, Set<Char>>, keyDistancesWithOpenDoors: Map<Char, Map<Char, Int>>): Map<Char, Map<Set<Char>, Int>> {
        var completion: Map<Char, Map<Set<Char>, Int>> = alphabet.filter { keysThatNeed[it].orEmpty().isEmpty() }.associateWith { mapOf(setOf(it) to 0) }

        fun lookup(oldPos: Char): Map<Set<Char>, Int> {
            val keysNeeding = keysThatNeed[oldPos].orEmpty()
            val startFrom = keyDistancesWithOpenDoors[oldPos]!!
            val new = mutableMapOf<Set<Char>, Int>()

            completion.map { (np, nv) ->
                nv.filter { (laterKeys, _) -> oldPos !in laterKeys && keysNeeding.all { it in laterKeys } }
                        .asSequence()
                        .map { (ks, cost) ->
                            (ks + oldPos) to (startFrom[np]!! + cost)
                        }
                        .filter { (k, v) -> new[k]?.let { it > v } ?: true }
                        .forEach { (k, v) -> new[k] = v }
            }
            return new
        }

        repeat(alphabet.last - alphabet.first +1) {
            completion = (alphabet+'@').associateWith { oldPos -> lookup(oldPos) }
        }

        return completion
    }

    private fun keyRequirements(dependencies: Map<Char, Set<Char>>, start: List<Char>): Map<Char, Set<Char>> {
        fun Map<Char, Set<Char>>.find(current: Char, target: Char): List<Char>? = when (target) {
            in this[current]!! -> listOf(current)
            else               -> this[current]!!
                    .mapNotNull { this.find(it, target) }
                    .firstOrNull()
                    ?.let { it + current }
        }

        var keyRequirements: Map<Char, Set<Char>> = start.map { st ->
            (alphabet).associateWith {
                dependencies.find(st, it).orEmpty().map { it.toLowerCase() }.filter { it !in start }.toSet()
            }
                    .filterValues { it.isNotEmpty() }
        }
                .fold(mutableMapOf()) { acc, map -> acc.putAll(map); acc }

        repeat(15) {
            keyRequirements = keyRequirements.mapValues { (_, deps) -> deps + deps.flatMap { keyRequirements[it].orEmpty() } }
        }
        return keyRequirements
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

    private fun buildDependencyTree(distances: Map<Char, Map<Char, Int>>, start: List<Char>): Map<Char, Set<Char>> {
        val tree = mutableMapOf<Char, Set<Char>>()

        var open = start.toSet()
        val visited = open.toMutableSet()

        while (open.isNotEmpty()) {
            open = open.flatMap {
                val new = distances[it].orEmpty().keys.filter { it !in visited }.toSet()
                tree += it to new
                visited += new
                new
            }.toSet()
        }
        return tree.toSortedMap()
    }

}
