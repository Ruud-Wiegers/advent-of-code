package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntProgram
import adventofcode.util.permutations

fun main() = Day07.solve()

object Day07 : AdventSolution(2019, 7, "Amplification Circuit") {

    override fun solvePartOne(input: String): Int? {
        val program = parse(input)

        val permutations = permutations(0..4)

        return permutations.map { runChain(program, it) }.max()
    }

    private fun runChain(program: IntArray, permutation: List<Int>) =
            permutation.fold(0) { acc, ph -> run(program, ph, acc) }


    private fun run(program: IntArray, phase: Int, input: Int) =
            IntProgram(program)
                    .apply {
                        inputChannel.add(phase)
                        inputChannel.add(input)
                    }
                    .run()
                    .outputChannel
                    .last()


    override fun solvePartTwo(input: String): Int? {
        val program = parse(input)

        val permutations = permutations(5..9)

        return permutations.map { runLoop(program, it) }.max()
    }

    private fun runLoop(program: IntArray, permutation: List<Int>): Int {

        val a = IntProgram(program.clone())
        val b = IntProgram(program.clone(), a.outputChannel)
        val c = IntProgram(program.clone(), b.outputChannel)
        val d = IntProgram(program.clone(), c.outputChannel)
        val e = IntProgram(program.clone(), d.outputChannel)
        a.inputChannel = e.outputChannel

        val programs = listOf(a, b, c, d, e)
        programs.indices.forEach { programs[it].inputChannel.add(permutation[it]) }


        programs.forEach {
            while (it.inputChannel.isNotEmpty() && !it.halt) {
                it.step()
            }
        }
        a.inputChannel.add(0)

        while (!d.halt) {
            programs.forEach {
                while (it.outputChannel.isEmpty() && !it.halt) {
                    it.step()
                }
            }
        }

        return e.outputChannel.last()
    }



    private fun parse(input: String) = input
            .split(',')
            .map(String::toInt)
            .toIntArray()
}