package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve


fun main(args: Array<String>) {
    Day12.solve()
}

object Day12 : AdventSolution(2018, 12, "Subterranean Sustainability") {

    override fun solvePartOne(input: String): Int {
        val (initial, rules) = parse(input)
        val g = 20
        return generateSequence(initial) { ("....$it....").windowedSequence(5) { rules[it]!! }.joinToString("") }
                .drop(g)
                .first()
                .mapIndexed { index, c -> if (c == '#') index - 2 * g else 0 }
                .sum()

    }

    override fun solvePartTwo(input: String): Int {
        val (initial, rules) = parse(input)
        val g = 1000
        return generateSequence(initial) { ("...$it...").windowedSequence(5) { rules[it]!! }.joinToString("") }
                .drop(g)
                .first()
                .mapIndexed { index, c -> if (c == '#') index - g else 0 }
                .sum()

    }

    private fun parse(input: String): Pair<String, Map<String, Char>> {
        val lines = input.split("\n")

        val initial = lines[0].substringAfter("initial state: ")
        val rules = lines.drop(2).map { it.split(" => ") }.associate { (cfg, n) -> cfg to n[0] }
        return initial to rules
    }

}
