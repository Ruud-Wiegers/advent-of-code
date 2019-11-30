package adventofcode.y2016

import adventofcode.AdventSolution

object Day10 : AdventSolution(2016, 10, "Balance Bots") {
    override fun solvePartOne(input: String): Int? {
        val (chips, lowInstr, highInstr) = parse(input)

        while (chips.values.any { it.size > 1 }) {
            step(chips, lowInstr, highInstr)
            chips.entries.find { it.value == listOf(17, 61) }
                    ?.let { return it.key }
        }
        return null
    }

    override fun solvePartTwo(input: String): Int {
        val (chips, lowInstr, highInstr) = parse(input)

        while (chips.values.any { it.size > 1 })
            step(chips, lowInstr, highInstr)

        return (0..2)
                .map { -it - 1 }
                .map(chips::getValue)
                .map(List<Int>::first)
                .reduce(Int::times)
    }

	private fun step(chips: MutableMap<Int, List<Int>>, lowInstr: Map<Int, Int>, highInstr: Map<Int, Int>) {
        chips
                .filter { it.value.size > 1 }
                .forEach { (bot, ch) ->
                    chips[bot] = emptyList()

                    val (lowChip, highChip) = ch

                    chips.merge(lowInstr.getValue(bot), listOf(lowChip)) { a, b -> (a + b).sorted() }
                    chips.merge(highInstr.getValue(bot), listOf(highChip)) { a, b -> (a + b).sorted() }
                }
    }

    private fun parse(input: String): Triple<MutableMap<Int, List<Int>>, Map<Int, Int>, Map<Int, Int>> {
        val lines = input.lineSequence()

        val valuePattern = ("value (\\d+) goes to bot (\\d+)").toRegex()
        val chips = lines
                .mapNotNull { valuePattern.matchEntire(it) }
                .groupBy({ it.groupValues[2].toInt() }, { it.groupValues[1].toInt() })
                .mapValues { it.value.sorted() }
                .toMutableMap()

        val instructionPattern = ("bot (\\d+) gives low to (output|bot) (\\d+) and high to (output|bot) (\\d+)").toRegex()

        val low = lines
                .mapNotNull { instructionPattern.matchEntire(it)?.destructured }
                .associate { (b0, t1, b1) -> b0.toInt() to if (t1 == "bot") b1.toInt() else -b1.toInt() - 1 }

        val high = lines
                .mapNotNull { instructionPattern.matchEntire(it)?.destructured }
                .associate { (b0, _, _, t1, b1) -> b0.toInt() to if (t1 == "bot") b1.toInt() else -b1.toInt() - 1 }
        return Triple(chips, low, high)
    }
}
