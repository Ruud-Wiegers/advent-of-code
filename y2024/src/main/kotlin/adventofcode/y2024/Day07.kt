package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day07.solve()
}

object Day07 : AdventSolution(2024, 7, "Bridge Repair") {

    override fun solvePartOne(input: String): Long = parseInput(input)
        .filter { it.hasSolution(Long::plus, Long::times) }
        .sumOf { it.solution }

    override fun solvePartTwo(input: String): Long = parseInput(input)
        .filter { it.hasSolution(Long::plus, Long::times, Long::combine) }
        .sumOf { it.solution }
}

private fun parseInput(input: String): List<Equation> = input.lines().map {
    val numbers = it.split(": ", " ").map(String::toLong)
    Equation(numbers.first(), numbers.drop(1))
}

private data class Equation(val solution: Long, val operands: List<Long>) {
    fun hasSolution(vararg operators: (Long, Long) -> Long): Boolean {

        fun solutions(values: List<Long>): Boolean = when {
            values.size == 1 -> values.last() == solution
            values.last() > solution -> false
            else -> operators.any { operator ->
                val newValues = values.toMutableList()
                newValues.addLast(operator(newValues.removeLast(), newValues.removeLast()))
                solutions(newValues)
            }
        }

        return solutions(operands.asReversed())
    }
}
infix fun Long.combine(o: Long) = (toString() + o.toString()).toLong()
