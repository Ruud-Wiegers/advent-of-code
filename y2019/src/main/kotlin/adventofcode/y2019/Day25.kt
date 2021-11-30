package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.util.IntCodeProgram

fun main() {
    Day25.solve()
}

object Day25 : AdventSolution(2019, 25, "Cryostasis") {

    override fun solvePartOne(input: String): String {
        val foundItems = listOf("fixed point", "whirled peas", "hologram", "shell", "fuel cell", "polygon", "antenna", "candy cane")
        return testItems(foundItems)
                .map {
                    val cmds = it.map { "drop $it" }.joinToString("\n")
                    val p = runAdventure(input, cmds)
                    p.readline("west")
                    p.execute()
                    p
                }
                .map { generateSequence(it::output).map { it.toInt().toChar() }.joinToString("") }
                .first { "heavier" !in it && "lighter" !in it }
    }

    private const val n = "north"
    private const val e = "east"
    private const val s = "south"
    private const val w = "west"

    private fun runAdventure(input: String, items: String): IntCodeProgram {
        return IntCodeProgram.fromData(input).apply {
            readline(listOf(
                    s, "take fixed point",
                    n, w, w, w, "take hologram",
                    e, e, e, n, "take candy cane",
                    w, "take antenna",
                    s, "take whirled peas",
                    n, w, "take shell",
                    e, e, n, n, "take polygon",
                    s, w, "take fuel cell",
                    w).joinToString("\n"))
            readline(items)
            execute()
            generateSequence(this::output).count()
        }
    }

    private fun testItems(rem: List<String>): Sequence<List<String>> = if (rem.isEmpty())
        sequenceOf(emptyList())
    else testItems(rem.drop(1)) + testItems(rem.drop(1)).map { it + rem[0] }


    private fun IntCodeProgram.readline(s: String) {
        (s + '\n').map { it.code.toLong() }.forEach(this::input)
    }

    override fun solvePartTwo(input: String) = "Free star!"

}
