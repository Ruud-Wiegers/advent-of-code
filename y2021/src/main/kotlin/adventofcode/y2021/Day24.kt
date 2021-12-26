package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve
import java.util.Stack

fun main() {
    Day24.solve()
}

object Day24 : AdventSolution(2021, 24, "Arithmetic Logic Unit") {

    override fun solvePartOne(input: String) = "36969794979199".also {
        require(verify(parse(input), it.map(Character::getNumericValue).iterator()))
        require(solveDecompiled(parseDifferences(input), it.map(Character::getNumericValue)))
    }

    override fun solvePartTwo(input: String) = "11419161313147".also {
        require(verify(parse(input), it.map(Character::getNumericValue).iterator()))

        require(solveDecompiled(parseDifferences(input), it.map(Character::getNumericValue)))
    }

    private data class Instruction(val op: String, val target: Char, val source: (memory: Map<Char, Long>) -> Long)

    private fun parse(input: String) = input.lineSequence().map { it.split(' ') }.map {
        val op = it[0]
        val target = it[1][0]
        val readSource = it.getOrNull(2)
        Instruction(op, target, when {
            readSource == null -> { _ -> throw IllegalStateException() }
            readSource.toLongOrNull() == null -> { m -> m.getValue(readSource[0]) }
            else -> { _ -> readSource.toLong() }
        })
    }

    private fun verify(program: Sequence<Instruction>, input: Iterator<Int>): Boolean {
        val memory = mutableMapOf('w' to 0L, 'x' to 0L, 'y' to 0L, 'z' to 0L)

        program.forEach { (op, target, source) ->

            when (op) {
                "inp" -> memory[target] = input.next().toLong()
                "add" -> memory[target] = memory.getValue(target) + source(memory)
                "mul" -> memory[target] = memory.getValue(target) * source(memory)
                "div" -> memory[target] = memory.getValue(target) / source(memory)
                "mod" -> memory[target] = memory.getValue(target) % source(memory)
                "eql" -> memory[target] = if (memory.getValue(target) == source(memory)) 1 else 0
            }
        }
        return memory['z'] == 0L
    }

    private fun parseDifferences(input: String) = input
        .split("inp w\n")
        .filter { it.isNotBlank() }
        .map {
            it.lines().filter { it.isNotBlank() }
        }
        .transpose()
        .filterNot { it.distinct().size == 1 }
        .transpose()
        .map { it.map { it.substringAfterLast(' ').toInt() } }
}

private fun <T> List<List<T>>.transpose(): List<List<T>> =
    first().indices.map { index -> map { it[index] } }

//hand-decompiled behavior of program
private fun solveDecompiled(program: List<List<Int>>, inputs: List<Int>): Boolean {
    val z = Stack<Int>()
    z.push(0)

    program.zip(inputs).forEach { (v, input) ->
        val a = v[1] + if (v[0] == 1) z.peek() else z.pop()
        if (a != input) z.push(input + v[2])
    }

    return z.pop() == 0 && z.isEmpty()
}

/*
further analysis: 7 pushes, 7 pops, 7 conditional pushes. z == 0 -> stack is empty
so conditional pushes must not occur.
the conditions compare values of previously stored digits. Further analysis yields:
S2  = S3  + 3
S4  = S11 + 8
S6  = S7  + 5
S8  = S9  + 2
S10 = S5  + 2
S12 = S1  + 3
S13 = S0  + 6

For part 1, we maximize the right side (9's)
For part 2, we minimize the left side (1's)
*/