package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.solve
import java.lang.IllegalStateException

fun main() {
    Day11.solve()
}

object Day11 : AdventSolution(2022, 11, "Monkey in the Middle") {

    override fun solvePartOne(input: String): Long = solve(input, 20,3)
    override fun solvePartTwo(input: String): Long = solve(input, 10_000,1)

    private fun solve(input: String, rounds: Int, reduction:Int): Long {
        val monkies = parse(input).sortedBy { it.id }
        val counts = monkies.map { 0L }.toMutableList()

        val mod = monkies.map { it.test.mod }.reduce(Long::times)

        repeat(rounds) {
            monkies.forEach { m ->
                m.items.forEach { item ->

                    // division under modulo isn't actually allowed here (no inverse) but whatever,
                    // part 1 stays small enough that the modulo doesn't kick in
                    val newLevel = (m.operation.apply(item) / reduction ) % mod
                    val newMonkey = m.test.test(newLevel)
                    monkies[newMonkey].items.add(newLevel)
                    counts[m.id]++
                }
                m.items.clear()
            }
        }
        return counts.sorted().takeLast(2).reduce(Long::times)
    }


    private fun parse(input: String): List<Monkey> {
        return input.split("\n\n")
            .map { it.toMonkey() }
    }

    private fun String.toMonkey(): Monkey {
        val lines = split("\n").map { it.trim() }
        val id = lines[0].substringAfter(' ').dropLast(1).toInt()
        val items = lines[1].substringAfter(": ").split(", ").map { it.toLong() }
        val operator = lines[2].toOperation()
        val mod = lines[3].substringAfter("by ").toLong()
        val t = lines[4].substringAfterLast(" ").toInt()
        val f = lines[5].substringAfterLast(" ").toInt()

        return Monkey(id, items.toMutableList(), operator, Test(mod, t, f))
    }
}

private data class Monkey(
    val id: Int,
    val items: MutableList<Long>,
    val operation: Operation,
    val test: Test
)

private data class Test(val mod: Long, val t: Int, val f: Int) {
    fun test(input: Long) = if (input % mod == 0L) t else f
}

private data class Operation(val operator: Char, val operand: Long?) {

    fun apply(input: Long) = when (operator) {
        '+' -> (input + (operand ?: input))
        '*' -> (input * (operand ?: input))
        else -> throw IllegalStateException(operator.toString())
    }
}

private fun String.toOperation(): Operation {
    val (operatorStr, operandStr) = substringAfter("new = old ").split(" ")
    return Operation(operatorStr[0], operandStr.toLongOrNull())
}
