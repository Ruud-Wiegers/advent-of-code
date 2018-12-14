package adventofcode.y2018

import adventofcode.AdventSolution

object Day14 : AdventSolution(2018, 14, "Chocolate Charts") {

    override fun solvePartOne(input: String): String {
        val recipes = mutableListOf(3, 7)
        var e1 = 0
        var e2 = 1

        while (recipes.size < input.toInt() + 10) {
            val res = recipes[e1] + recipes[e2]
            if (res > 9) recipes += res / 10
            recipes += res % 10

            e1 = (e1 + recipes[e1] + 1) % recipes.size
            e2 = (e2 + recipes[e2] + 1) % recipes.size
        }
        return recipes.drop(input.toInt()).take(10).joinToString("")

    }

    override fun solvePartTwo(input: String): Int {
        val target = input.map { it - '0' }
        val recipes = mutableListOf(3, 7)
        var e1 = 0
        var e2 = 1
        while (true) {
            val res = recipes[e1] + recipes[e2]
            if (res > 9) {
                recipes += res / 10
                if (recipes.endMatches(target)) break
            }
            recipes += res % 10
            if (recipes.endMatches(target)) break

            e1 = (e1 + recipes[e1] + 1) % recipes.size
            e2 = (e2 + recipes[e2] + 1) % recipes.size
        }
        return recipes.size - target.size
    }

    //faster version of takeLast(end.size)==end
    private fun List<Int>.endMatches(end: List<Int>) =
            this.size >= end.size && end.indices.all { end[it] == this[size - end.size + it] }
}
