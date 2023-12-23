package adventofcode.y2019

import adventofcode.io.AdventSolution
import adventofcode.language.intcode.IntCodeProgram
import adventofcode.util.collections.cycle
import adventofcode.util.collections.permutations

fun main() = Day07.solve()

object Day07 : AdventSolution(2019, 7, "Amplification Circuit") {

    override fun solvePartOne(input: String) = solve(input, 0L..4L)

    override fun solvePartTwo(input: String) = solve(input, 5L..9L)

    private fun solve(input: String, phases: LongRange) =
        phases.permutations()
            .map { permutation -> setupPrograms(input, permutation) }
            .map { programs -> runLoop(programs) }
            .maxOrNull()


    private fun setupPrograms(data: String, permutation: List<Long>): List<IntCodeProgram> =
        permutation.map { phase -> IntCodeProgram.fromData(data).apply { input(phase) } }

    private fun runLoop(programs: List<IntCodeProgram>): Long = programs.cycle()
        .takeWhile { it.state != IntCodeProgram.State.Halted }
        .fold(0L) { power, program ->
            program.input(power)
            program.execute()
            program.output()!!
        }
}
