package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.abs



fun main()=Day25.solve()
object Day25 : AdventSolution(2018, 25, "Four-Dimensional Adventure") {

    override fun solvePartOne(input: String): Int {
        val constellations = input.splitToSequence('\n', ',')
                .map(String::toInt)
                .chunked(4)
                .map { mutableListOf(it) }
                .toMutableList()

        do {
            constellations.removeIf { it.isEmpty() }
            for (src in constellations.indices) {
                for (target in 0 until src) {

                    val linked = constellations[src].any { s ->
                        constellations[target].any { t ->
                            distance(s, t) <= 3
                        }
                    }

                    if (linked) {
                        constellations[target].addAll(constellations[src])
                        constellations[src].clear()
                        break
                    }
                }
            }
        } while (constellations.any { it.isEmpty() })

        return constellations.size

    }

    override fun solvePartTwo(input: String) = "Free Star! ^_^"
}

private fun distance(xs: List<Int>, ys: List<Int>) = xs.zip(ys) { x, y -> abs(x - y) }.sum()



