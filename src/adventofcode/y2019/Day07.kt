package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram
import adventofcode.util.collections.cycle
import adventofcode.util.collections.permutations

fun main() = Day07.solve()

object Day07 : AdventSolution(2019, 7, "Amplification Circuit") {

    override fun solvePartOne(input: String) = solve(input, 0L..4L)

    override fun solvePartTwo(input: String) = solve(input, 5L..9L)

    private fun solve(input: String, phases: LongRange): Long? {
        val data = input.split(',').map(String::toLong)
        val permutations = phases.permutations()

        return permutations
                .map { permutation -> setupPrograms(permutation, data) }
                .map { programs -> runLoop(programs) }
                .max()
    }

    private fun setupPrograms(permutation: List<Long>, data: List<Long>): List<IntCodeProgram> = permutation
            .map { phase ->
                IntCodeProgram(data).apply { input(phase) }
            }

    private fun runLoop(programs: List<IntCodeProgram>): Long = programs.cycle()
            .takeWhile { it.state != IntCodeProgram.State.Halted }
            .fold(0L) { power, program ->
                program.input(power)
                program.execute()
                program.output()!!
            }
}
