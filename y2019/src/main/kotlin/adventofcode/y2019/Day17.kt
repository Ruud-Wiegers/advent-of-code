package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.language.intcode.IntCodeProgram
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

fun main() = Day17.solve()

object Day17 : AdventSolution(2019, 17, "Set and Forget") {

    override fun solvePartOne(input: String): Int {
        val map = readMap(IntCodeProgram.fromData(input))

        return (1 until map.lastIndex).sumOf { y ->
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

    override fun solvePartTwo(input: String): Long {
        val data = "2" + input.drop(1)
        val program = IntCodeProgram.fromData(data)
        val map: List<String> = readMap(program)
        val route = findRoute(map)
                .joinToString(",")

        val v = CompressedRoute(route).fullCompress("ABC").first { it.fitsInMemory() && it.fullyCompressed() }

        val instructions = listOf(v.main) + v.functions + "n"
        instructions.joinToString("\n", postfix = "\n").forEach { program.input(it.code.toLong()) }
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
        generateSequence { output() }.map { it.toInt().toChar() }.joinToString("")
    }
            .lines().filter { it.isNotBlank() }

    private fun findRoute(map: List<String>): List<String> {
        operator fun List<String>.get(p: Vec2) = this.getOrNull(p.y)?.getOrNull(p.x)
        fun path(v: Vec2) = map[v] == '#'

        val y = map.indexOfFirst { '^' in it }
        val x = map[y].indexOf('^')


        val position = Vec2(x, y)
        val direction = Direction.UP

        return generateSequence<Pair<Direction?, Vec2>>(direction to position) { (od, op) ->
            when {
                od == null                     -> null
                path(op + od.vector)           -> od to op + od.vector
                path(op + od.turnLeft.vector)  -> od.turnLeft to op
                path(op + od.turnRight.vector) -> od.turnRight to op
                else                           -> null to op
            }
        }
                .zipWithNext { a, b -> a.takeIf { a.second == b.second } }
                .filterNotNull()
                .zipWithNext { a, b ->
                    sequenceOf(
                            if (a.first?.turnLeft == b.first) "L" else "R",
                            a.second.distance(b.second).toString())
                }
                .flatten()
                .toList()
    }
}
