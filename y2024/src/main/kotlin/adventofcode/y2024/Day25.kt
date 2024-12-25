package adventofcode.y2024

import adventofcode.io.AdventSolution
import adventofcode.util.algorithm.transposeString
import adventofcode.util.collections.cartesian

fun main() {
    Day25.solve()
}

object Day25 : AdventSolution(2024, 25, "Code Chronicle") {

    override fun solvePartOne(input: String): Int {

        val (keyStr, lockStr) = input.split("\n\n").map(String::lines).partition { it[0][0] == '#' }

        fun pinLengths(image: List<String>) = image.transposeString().map { pin -> pin.count('#'::equals) }
        val keys = keyStr.map(::pinLengths)
        val locks = lockStr.map(::pinLengths)

        return keys.cartesian(locks).count { (key, lock) ->
            key.zip(lock, Int::plus).all { it <= 7 }
        }
    }

    override fun solvePartTwo(input: String) = "Free Star!"
}