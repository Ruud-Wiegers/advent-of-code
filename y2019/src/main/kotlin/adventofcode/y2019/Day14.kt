package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.ceil

fun main() = Day14.solve()

object Day14 : AdventSolution(2019, 14, "Space Stoichiometry") {

    override fun solvePartOne(input: String) = calculateOre(1L, parse(input))

    override fun solvePartTwo(input: String): Long {
        val reactions = parse(input)
        val target = 1_000_000_000_000
        val bounds = 1..10_000_000L
        return bounds.binarySearch { calculateOre(it, reactions) < target }.first
    }

    private inline fun LongRange.binarySearch(isBelowTarget: (Long) -> Boolean): LongRange {
        check(isBelowTarget(first))
        check(!isBelowTarget(last))

        var low = first
        var high: Long = last

        while (low + 1 < high) {
            val mid = (low + high) / 2
            if (isBelowTarget(mid)) low = mid else high = mid
        }

        return low..high
    }


    private fun calculateOre(fuel: Long, reactions: Map<String, Pair<Int, List<Term>>>): Long {
        val requirements = mutableMapOf("FUEL" to fuel)
        var requiredOreCount = 0L

        while (requirements.any { it.value > 0L }) {
            val productToCreate = requirements.asIterable().first { it.value > 0L }.key
            val required = requirements.remove(productToCreate)!!
            val (amount, reagents) = reactions.getValue(productToCreate)
            val times = ceil(required / amount.toDouble()).toLong()

            requirements[productToCreate] = required - amount * times

            reagents.forEach { requirements.merge(it.unit, it.amount * times, Long::plus) }

            requiredOreCount += requirements.remove("ORE") ?: 0
        }
        return requiredOreCount
    }


    private fun parse(input: String) = input
            .lineSequence()
            .map { it.split(" => ") }
            .associate { (reagents, result) ->
                val (amount, unit) = parseTerm(result)
                unit to Pair(amount, reagents.split(", ").map(this::parseTerm))
            }

    private fun parseTerm(input: String) = input.split(" ").let { Term(it[0].toInt(), it[1]) }
    private data class Term(val amount: Int, val unit: String)

}
