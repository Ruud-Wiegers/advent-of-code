package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day12.solve()
}

object Day12 : AdventSolution(2023, 12, "Hot Springs") {

    override fun solvePartOne(input: String) = parse(input).sumOf(ConditionRecord::countMatchingConfigurations)

    override fun solvePartTwo(input: String) = parse(input, 5).sumOf(ConditionRecord::countMatchingConfigurations)
}

private fun parse(input: String, times: Int = 1) = input.lineSequence().map {
    val (line, guide) = it.split(" ")

    val springs = "$line?".repeat(times).dropLast(1).map(Spring::fromChar)
    val groups = "$guide,".repeat(times).dropLast(1).split(",").map(String::toInt)

    ConditionRecord(springs, groups)
}

private data class ConditionRecord(val row: List<Spring>, val damagedGroups: List<Int>) {
    fun countMatchingConfigurations() =
        row.fold(mapOf(SolutionPattern(damagedGroups, Spring.Unknown) to 1L)) { prev, spring ->
            buildMap {
                for ((clue, count) in prev) {
                    for (new in clue.matchNextSpring(spring)) {
                        merge(new, count, Long::plus)
                    }
                }
            }
        }
            .filter { it.key.damagedGroups.isEmpty() }.values.sum()
}


private data class SolutionPattern(val damagedGroups: List<Int>, val requiredNext: Spring) {

    fun matchNextSpring(spring: Spring): List<SolutionPattern> = when (spring) {
        Spring.Operational -> matchOperationalSpring()
        Spring.Damaged -> matchDamagedSpring()
        Spring.Unknown -> matchOperationalSpring() + matchDamagedSpring()
    }

    private fun matchOperationalSpring(): List<SolutionPattern> =
        if (requiredNext == Spring.Damaged) emptyList()
        else copy(requiredNext = Spring.Unknown).let(::listOf)

    private fun matchDamagedSpring(): List<SolutionPattern> = when {
        requiredNext == Spring.Operational -> emptyList()
        damagedGroups.isEmpty() -> emptyList()
        damagedGroups.first() == 1 -> SolutionPattern(damagedGroups.drop(1), Spring.Operational).let(::listOf)
        else -> SolutionPattern(damagedGroups.toMutableList().also { it[0]-- }, Spring.Damaged).let(::listOf)
    }
}


private enum class Spring {
    Operational, Damaged, Unknown;

    companion object {
        fun fromChar(ch: Char) = when (ch) {
            '.' -> Operational
            '#' -> Damaged
            else -> Unknown
        }
    }
}
