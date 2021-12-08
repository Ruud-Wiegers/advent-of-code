package adventofcode.y2018

import adventofcode.AdventSolution

object Day12 : AdventSolution(2018, 12, "Subterranean Sustainability") {

    override fun solvePartOne(input: String): Int {
        val (initial, rules) = parse(input)
        val g = 20
        return generateSequence(initial) { ("....$it....").windowedSequence(5) { w -> rules[w] }.joinToString("") }
                .drop(g)
                .first()
                .mapIndexed { index, c -> if (c == '#') index - 2 * g else 0 }
                .sum()
    }

    override fun solvePartTwo(input: String): Long {
        val (initial, rules) = parse(input)
        val gen = 200
        val generations = generateSequence(initial) { prev ->
            ("...$prev...")
                    .windowedSequence(5) { rules[it]!! }
                    .joinToString("")
        }

        val scores = generations.mapIndexed { i, v ->
            v.mapIndexed { index, c -> if (c == '#') index - i else 0 }.sum()
        }

        val stableScores = scores
                .drop(gen)
                .take(5)
                .toList()

        //stabilized?
        check(stableScores.zipWithNext(Int::minus).distinct().size == 1)

        return stableScores[0] + (50_000_000_000 - gen) * (stableScores[1] - stableScores[0])
    }

    private fun parse(input: String): Pair<String, Map<String, Char>> {
        val lines = input.lines()

        val initial = lines[0].substringAfter("initial state: ")
        val rules = lines.drop(2).map { it.split(" => ") }.associate { (cfg, n) -> cfg to n[0] }
        return initial to rules
    }
}
