package adventofcode.y2025

import adventofcode.io.AdventSolution

fun main() {
    Day07.solve()
}

object Day07 : AdventSolution(2025, 7, "Laboratories") {

    override fun solvePartOne(input: String): Any {
        val grid = input.lines()
        val start = grid[0].indexOf('S')

        var splitCount = 0

        var beams = setOf(start)
        for (y in 1..grid.lastIndex) {
            beams = buildSet {
                for (x in beams) {
                    if (grid[y][x] == '^') {
                        splitCount++
                        add(x - 1)
                        add(x + 1)
                    } else {
                        add(x)
                    }
                }
            }
        }
        return splitCount
    }

    override fun solvePartTwo(input: String): Long {
        val grid = input.lines()
        val initialPathCounts = grid[0].map { if (it == 'S') 1L else 0L }

        val paths = grid.drop(1).fold(initialPathCounts) { prevPathCounts, line ->
            val padded = buildList {
                add(Pair('.', 0L))
                addAll(line.toList().zip(prevPathCounts))
                add(Pair('.', 0L))
            }

            padded.windowed(3).map { (l, m, r) ->
                var sum = 0L
                if (l.first == '^') sum += l.second
                if (m.first != '^') sum += m.second
                if (r.first == '^') sum += r.second
                sum
            }
        }

        return paths.sum()
    }
}