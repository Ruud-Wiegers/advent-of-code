package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day22.solve()
}

object Day22 : AdventSolution(2024, 22, "???") {

    override fun solvePartOne(input: String): Long {
        return input.lines().sumOf { generateSequence(it.toLong()) { calculate(it) }.elementAt(2000) }
    }

    override fun solvePartTwo(input: String): Int {
        val buyers = input.lines().map {
            generateSequence(it.toLong()) { calculate(it) }.drop(1).take(2000).map { (it % 10).toInt() }.toList()
        }

        val buyerDeltas = buyers.map { it.zipWithNext { a, b -> b - a }.windowed(4) }

        fun score(pattern: List<Int>): Int {
            return buyerDeltas.mapIndexed { index, v ->
                val i = v.indexOf(pattern)

                if (i < 0) 0 else buyers[index][i + 4]
            }.sum()
        }


        return patterns().maxOf { score(it) }

    }

    private fun calculate(input: Long): Long {
        var step = input
        step = (step xor step * 64) % 16777216
        step = (step xor step / 32) % 16777216
        step = (step xor step * 2048) % 16777216
        return step
    }

    private fun patterns() = buildList {
        for (a in -9..9)
            for (b in -9..9)
                for (c in -9..9)
                    for (d in -9..9) {
                        add(listOf(a, b, c, d))
                    }

    }.sortedByDescending(List<Int>::sum).dropWhile { it.sum() > 9 }.takeWhile { it.sum() > 0 }


}