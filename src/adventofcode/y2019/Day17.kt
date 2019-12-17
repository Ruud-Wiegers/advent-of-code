package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() = Day17.solve()

object Day17 : AdventSolution(2019, 17, "Arcade") {

    override fun solvePartOne(input: String): Long {
        val map = IntCodeProgram.fromData(input).run {
            execute()
            generateSequence { output() }.map { it.toChar() }.joinToString("")
        }
                .lines()
                .filterNot { it.isBlank() }

        var sum = 0L
        for (y in 1 until map.lastIndex) {
            for (x in 1 until map[y].lastIndex) {
                if (map[y][x] == '#' && map[y - 1][x] == '#' && map[y][x - 1] == '#' && map[y + 1][x] == '#' && map[y][x + 1] == '#')
                    sum += y * x
            }
        }
        return sum
    }


    override fun solvePartTwo(input: String): Long {
        val map = IntCodeProgram.fromData(input).run {
            execute()
            generateSequence { output() }.map { it.toChar() }.joinToString("")
        }
                .lines()
                .filterNot { it.isBlank() }

        println()
        map.forEach { println(it) }

        return 0L

    }

}


