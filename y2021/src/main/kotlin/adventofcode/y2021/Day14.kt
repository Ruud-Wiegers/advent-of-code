package adventofcode.y2021

import adventofcode.AdventSolution


object Day14 : AdventSolution(2021, 14, "Extended Polymerization") {
    override fun solvePartOne(input: String): Int {
        val (initial, rules) = parse(input)

        fun step(str: String) = str.windowed(2).joinToString("") { s -> s[0] + rules[s].orEmpty() } + str.last()

        val freq = generateSequence(initial, ::step).elementAt(10).groupingBy { it }.eachCount()

        return freq.maxOf { it.value } - freq.minOf { it.value }
    }

    override fun solvePartTwo(input: String): Long {
        val (a, rules) = parse(input)

        val initial = a.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

        fun next(current: Map<String, Long>) =
            current.flatMap { (p, c) ->
                (rules[p])?.let { listOf((p[0] + it) to c, (it + p[1]) to c) } ?: listOf(p to c)
            }
                .groupingBy { it.first }
                .fold(0L) { a, v -> a + v.second }

        val resultPairs = generateSequence(initial, ::next).elementAt(40)
        val freq = resultPairs.asSequence()
            .groupingBy { it.key[0] }
            .fold(0L) { s, v -> s + v.value }
            .toMutableMap()
        freq[a.last()] = freq.getValue(a.last()) + 1

        return freq.maxOf { it.value } - freq.minOf { it.value }
    }

    private fun parse(input: String): Pair<String, Map<String, String>> {
        val (a, b) = input.split("\n\n")

        val rules = b.lineSequence().map { it.split(" -> ") }
            .associate { it[0] to it[1] }
        return a to rules
    }
}
