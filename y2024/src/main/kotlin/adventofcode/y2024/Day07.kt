package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day07.solve()
}

object Day07 : AdventSolution(2024, 7, "Bridge Repair") {

    override fun solvePartOne(input: String): Long {
        val equations = parseInput(input)



        return equations.filter { it.hasSolution() }
            .sumOf { it.solution }
    }

    override fun solvePartTwo(input: String): Long {
        val equations = parseInput(input)



        return equations.filter { it.hasSolution2() }
            .sumOf { it.solution }
    }
}

private fun Equation.hasSolution(): Boolean {
    fun solutions(operands: List<Long>): List<Long> {
        if(operands.first() > solution) return emptyList()
        if (operands.size == 1) return operands

        val (a, b) = operands
        val rem = operands.drop(2)
        return solutions(listOf(a + b) + rem) + solutions(listOf(a * b) + rem)
    }

    return solution in solutions(operands)
}

private fun Equation.hasSolution2(): Boolean {
    infix fun Long.combine(o: Long) = (toString() + o.toString()).toLong()

    fun solutions(operands: List<Long>): List<Long> {
        if (operands.size == 1) return operands
        if(operands.first() > solution) return emptyList()
        val (a, b) = operands
        val rem = operands.drop(2)
        return solutions(listOf(a + b) + rem) + solutions(listOf(a * b) + rem) + solutions(listOf(a combine b) + rem)
    }


    return solution in solutions(operands)
}


private fun parseInput(input: String): List<Equation> = input.lines().map {
    val numbers = it.split(": ", " ").map(String::toLong)
    Equation(numbers.first(), numbers.drop(1))
}


private data class Equation(val solution: Long, val operands: List<Long>)