package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntProgram
import adventofcode.util.collections.cycle
import adventofcode.util.collections.permutations

fun main() = Day07.solve()

object Day07 : AdventSolution(2019, 7, "Amplification Circuit") {

    override fun solvePartOne(input: String) = solve(input, 0..4)

    override fun solvePartTwo(input: String) = solve(input, 5..9)

    private fun solve(input: String, phases: IntRange): Int? {
        val data = input.split(',').map(String::toInt)
        val permutations = phases.permutations()

        return permutations
                .map { permutation -> setupPrograms(permutation, data) }
                .map { programs -> runLoop(programs) }
                .max()
    }

    private fun setupPrograms(permutation: List<Int>, data: List<Int>): List<IntProgram> = permutation
            .map { phase ->
                IntProgram(data.toIntArray()).apply { input(phase) }
            }

    private fun runLoop(programs: List<IntProgram>): Int = programs.cycle()
            .takeWhile { it.state != IntProgram.State.Halted }
            .fold(0) { power, program ->
                program.input(power)
                program.execute()
                program.output()!!
            }
}