package adventofcode.y2019

import adventofcode.io.AdventSolution

fun main() = Day04.solve()

object Day04 : AdventSolution(2019, 4, "Secure Container") {

    override fun solvePartOne(input: String) = parseFast(input).count { c ->
        c.toString().zipWithNext().any { (a, b) -> a == b }
    }

    override fun solvePartTwo(input: String) = parseFast(input).count { c ->
        c.toString().groupBy { it }.any { it.value.size == 2 }
    }

    private fun parseFast(input: String): Sequence<Int> {
        val (l, h) = input.split('-').map(String::toInt)
        return iterate(h).dropWhile { it < l }
    }


    private fun iterate(bound: Int) = sequence {
        outer@ for (a in 0..9)
            for (b in a..9)
                for (c in b..9)
                    for (d in c..9)
                        for (e in d..9)
                            for (f in e..9) {
                                val res = a * 100_000 + b * 10_000 + c * 1_000 + d * 100 + e * 10 + f
                                if (res > bound) break@outer
                                yield(res)
                            }
    }
}
