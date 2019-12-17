package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram
import adventofcode.util.collections.scan
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() = Day17.solve()

object Day17 : AdventSolution(2019, 17, "Set and Forget") {

    override fun solvePartOne(input: String): Int {
        val map = readMap(IntCodeProgram.fromData(input))

        return (1 until map.lastIndex).sumBy { y ->
            y * (1 until map[y].lastIndex)
                    .filter { x -> map.isCrossing(y, x) }
                    .sum()
        }
    }

    private fun List<String>.isCrossing(y: Int, x: Int): Boolean =
            this[y][x] == '#'
                    && this[y - 1][x] == '#'
                    && this[y][x - 1] == '#'
                    && this[y + 1][x] == '#'
                    && this[y][x + 1] == '#'

    override fun solvePartTwo(input: String): Long? {
        val data = "2" + input.drop(1)
        val program = IntCodeProgram.fromData(data)
        val map: List<String> = readMap(program)
        val route = findRoute(map)
                .zipWithNext { (_, d0), (c1, d1) -> listOf(if (d0.turnLeft == d1) "L" else "R", c1.toString()) }
                .flatten()
                .joinToString(",")

        val v = CompressedRoute(route).fullCompress("ABC").first { it.fitsInMemory() && it.fullyCompressed() }

        val instructions = listOf(v.main) + v.functions + "n"
        instructions.joinToString("\n", postfix = "\n").forEach { program.input(it.toLong()) }
        program.execute()
        return generateSequence { program.output() }.last()
    }

    data class CompressedRoute(val main: String, val functions: List<String>, val compressionTokens: String) {

        constructor(route: String) : this(route, emptyList(), "")

        fun fullCompress(tokens: String) =
                tokens.fold(sequenceOf(this)) { possibleCompressions, token ->
                    possibleCompressions.flatMap { comp -> comp.greedyCompressionCandidates(token) }
                }

        private fun greedyCompressionCandidates(compressTo: Char): Sequence<CompressedRoute> =
                generateCandidatePrefixes().map { newFunction ->
                    CompressedRoute(main.replace(newFunction, compressTo.toString()),
                            functions + newFunction,
                            compressionTokens + compressTo)
                }

        private fun generateCandidatePrefixes(): Sequence<String> = main
                .splitToSequence(",")
                .dropWhile { it in compressionTokens }
                .takeWhile { it !in compressionTokens }
                .scan(emptyList<String>()) { a, n -> a + n }
                .map { it.joinToString(",") }

        fun fullyCompressed() = main.all { it in "$compressionTokens," }
        fun fitsInMemory() = (functions + main).all { it.length <= 20 }
    }


    private fun readMap(program: IntCodeProgram): List<String> = program.run {
        execute()
        generateSequence { output() }.map { it.toChar() }.joinToString("")
    }
            .lines().filter { it.isNotBlank() }

    private fun findRoute(map: List<String>): MutableList<Pair<Int, Direction>> {
        operator fun List<String>.get(p: Vec2) = this.getOrNull(p.y)?.getOrNull(p.x)

        val y = map.indexOfFirst { '^' in it }
        val x = map[y].indexOf('^')


        var position = Vec2(x, y)
        var direction = Direction.UP
        val steps = mutableListOf<Pair<Int, Direction>>()
        var count = 0


        while (true) {
            if (map[position + direction.vector] == '#') {
                count += 1
            } else if (map[position + direction.turnLeft.vector] == '#') {
                steps += count to direction
                count = 1
                direction = direction.turnLeft

            } else if (map[position + direction.turnRight.vector] == '#') {
                steps += (count to direction)
                count = 1
                direction = direction.turnRight

            } else {
                steps += (count to direction)
                break
            }
            position += direction.vector
        }
        return steps
    }
}
