package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.neighbors

fun main() = Day20.solve()

object Day20 : AdventSolution(2024, 20, "Race Condition") {

    override fun solvePartOne(input: String): Int {
        val grid = parseInput(input)
        val racetrack = path(grid)
        return countValidCheats(racetrack, 2)
    }

    override fun solvePartTwo(input: String): Int {
        val grid = parseInput(input)
        val racetrack = path(grid)
        return countValidCheats(racetrack, 20)
    }


    fun parseInput(input: String) = input.lines()
        .flatMapIndexed { y, line ->
            line.mapIndexed { x, c -> Vec2(x, y) to c }
        }
        .filter { (_, v) -> v in ".SE" }
        .toMap()

    fun path(grid: Map<Vec2, Char>): List<Vec2> {
        val start = grid.entries.first { it.value == 'S' }.key
        val track = grid.keys.toMutableSet()

        return buildList {
            add(start)
            track -= start
            while (track.isNotEmpty()) {
                val edge = this.last().neighbors().first { it in track }
                track -= edge
                add(edge)
            }
        }
    }

    fun countValidCheats(racetrack: List<Vec2>, maxCheatLength: Int, minCheatGain: Int = 100): Int {
        var count = 0

        for (oldIndex in racetrack.indices) {
            for (newIndex in oldIndex + minCheatGain..racetrack.lastIndex) {
                val cheatLength = racetrack[oldIndex].distance(racetrack[newIndex])
                val cheatGain = newIndex - oldIndex

                if (cheatLength > maxCheatLength) continue
                if (cheatGain - cheatLength >= minCheatGain) count++
            }
        }
        return count
    }
}
