package adventofcode.y2018

import adventofcode.AdventSolution

object Day14 : AdventSolution(2018, 14, "Chocolate Charts") {

    override fun solvePartOne(input: String): String {
        val targetSize = input.toInt() + 10
        return generateRecipes { it.size == targetSize }
                .takeLast(10)
                .joinToString("")
    }


    override fun solvePartTwo(input: String): Int {
        val target = input.map { it - '0' }
        val recipes = generateRecipes{it.endMatches(target)}
        return recipes.size - target.size
    }

    private fun generateRecipes(stop: (List<Int>)->Boolean): MutableList<Int> {
        val recipes = mutableListOf(3, 7)
        var e1 = 0
        var e2 = 1
        while (true) {
            for (it in (recipes[e1] + recipes[e2]).toString()) {
                recipes += it - '0'
                if (stop(recipes)) return recipes
            }
            e1 = (e1 + recipes[e1] + 1) % recipes.size
            e2 = (e2 + recipes[e2] + 1) % recipes.size
        }
    }

    //faster version of takeLast(end.size)==end
    private fun List<Int>.endMatches(end: List<Int>) =
            this.size >= end.size && end.indices.all { end[it] == this[size - end.size + it] }
}
