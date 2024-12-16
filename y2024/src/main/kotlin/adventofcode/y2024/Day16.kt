package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.plus


fun main() {
    Day16.solve()
}

object Day16 : AdventSolution(2024, 16, "Reindeer Maze") {

    override fun solvePartOne(input: String): Int {
        val parsed = parseInput(input)

        val start = parsed.entries.single { it.value == 'S' }.key
        val end = parsed.entries.single { it.value == 'E' }.key
        val grid = parsed.keys

        return costOfShortestPath(State(start, Direction.RIGHT), end, grid)

    }

    private fun costOfShortestPath(state: State, end: Vec2, grid: Set<Vec2>): Int {

        val open = sortedMapOf(0 to listOf(state))
        val seen = mutableSetOf(state)


        while (open.isNotEmpty()) {
            val lowestCost = open.firstKey()
            val candidates = open.remove(lowestCost)!!

            for (candidate in candidates) {
                if (candidate.p == end) return lowestCost
                candidate.neighbours()
                    .filterKeys { it.p in grid }
                    .filterKeys { it !in seen }
                    .mapValues { it.value + lowestCost }
                    .forEach {
                        seen += it.key
                        open.merge(it.value, listOf(it.key)) { a, b -> a + b }
                    }
            }
        }
        return -1
    }

    override fun solvePartTwo(input: String): Int {
        val parsed = parseInput(input)
        val start = parsed.entries.single { it.value == 'S' }.key
        val end = parsed.entries.single { it.value == 'E' }.key
        val grid = parsed.keys

        return shortestPaths(State(start, Direction.RIGHT), end, grid)
    }


    private fun shortestPaths(state: State, end: Vec2, grid: Set<Vec2>): Int {

        val open = sortedMapOf(0 to listOf(StateWithPath(state, setOf(state.p))))
        val seen = mutableMapOf(state to 0)

        while (open.isNotEmpty()) {
            val lowestCost = open.firstKey()
            val candidates = open.remove(lowestCost)!!

            if (candidates.any { it.state.p == end }) {
                return candidates.filter { it.state.p == end }.map { it.path }.reduce { a, b -> a + b }.size
            }

            for (candidate in candidates) {
                candidate.state.neighbours()
                    .filterKeys { it.p in grid }
                    .mapValues { it.value + lowestCost }
                    .filter { (seen[it.key] ?: Int.MAX_VALUE) >= it.value }
                    .forEach {
                        seen[it.key] = it.value
                        open.merge(it.value, listOf(StateWithPath(it.key, candidate.path + it.key.p))) { a, b -> a + b }
                    }
            }
        }
        return -1
    }

    fun parseInput(input: String): Map<Vec2, Char> = input.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Vec2(x, y) to c }
        }
        .filter { (_, v) -> v in "SE." }
        .toMap()
}


data class State(val p: Vec2, val d: Direction) {
    fun neighbours(): Map<State, Int> = mapOf(
        copy(d = d.turnLeft) to 1000,
        copy(d = d.turnRight) to 1000,
        copy(p = p + d) to 1
    )
}

data class StateWithPath(val state: State, val path: Set<Vec2>)