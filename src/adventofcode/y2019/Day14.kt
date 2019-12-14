package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.ceil

fun main() = Day14.solve()

object Day14 : AdventSolution(2019, 14, "Space Stoichiometry") {

    override fun solvePartOne(input: String) = calculateOre(input, 1L)


    override fun solvePartTwo(input: String): Long {
        var low = 1L
        var high = 3000000L

        while (low+1 < high) {
            val mid = (low + high) / 2
            val calculateOre = calculateOre(input, mid)
            if (calculateOre > 1000000000000)
                high = mid
            else low = mid
        }

        return low
    }


    private fun calculateOre(input: String, fuel: Long): Long {
        val parsed = parse(input)
        val amounts = parsed.keys.associate { it.unit to it.amount }
        val createdBy = parsed.mapKeys { it.key.unit }
        val products = sortedMapOf("FUEL" to fuel)

        var ORE = 0L

        while (products.any { it.value > 0L }) {
            val type = products.filter { it.value > 0L }.asIterable().first().key
            val required = products.remove(type)!!

            val times = ceil(required / amounts.getValue(type).toDouble()).toInt()
            val (ore, rem) = createdBy.getValue(type)
                    .map { it.copy(amount = it.amount * times) }
                    .partition { it.unit == "ORE" }
            ORE += ore.map { it.amount }.sum()

            products[type] = required - times * amounts.getValue(type)

            rem.forEach { (u, a) -> products.merge(a, u, Long::plus) }
        }
        return ORE
    }


    private fun parse(input: String) =
            input.lineSequence().map { it.split(" => ") }
                    .associate { (r, p) -> parseTerm(p) to r.split(", ").map { parseTerm(it) } }


    private fun parseTerm(input: String) = input.split(" ").let { Term(it[0].toLong(), it[1]) }
    private data class Term(val amount: Long, val unit: String)

}
