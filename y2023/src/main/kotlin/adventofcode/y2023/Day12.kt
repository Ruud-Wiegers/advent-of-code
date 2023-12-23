package adventofcode.y2023

import adventofcode.io.AdventSolution

fun main() {
    Day12.solve()
}

object Day12 : AdventSolution(2023, 12, "Hot Springs") {

    override fun solvePartOne(input: String) = parse(input).sumOf(ConditionRecord::countMatchingConfigurations)

    override fun solvePartTwo(input: String) = parse(input, 5).sumOf(ConditionRecord::countMatchingConfigurations)

    private fun parse(input: String, times: Int = 1) = input.lineSequence().map {
        val (line, guide) = it.split(" ")

        val springs = "$line?".repeat(times).dropLast(1).map(Spring::fromChar)
        val groups = "$guide,".repeat(times).dropLast(1).split(",").map(String::toInt)

        ConditionRecord(springs, groups)
    }
}


private data class ConditionRecord(val row: List<Spring>, val damagedGroups: List<Int>) {
    fun countMatchingConfigurations() =
        row.fold(mapOf(SolutionPattern(Constraint.None, damagedGroups) to 1L)) { prev, spring ->
            buildMap {
                for ((clue, count) in prev) {
                    for (new in clue.matchNextSpring(spring)) {
                        merge(new, count, Long::plus)
                    }
                }
            }
        }
            .filterKeys(SolutionPattern::completeMatch).values.sum()
}


private data class SolutionPattern(val constraint: Constraint, val unmatchedDamagedGroups: List<Int>) {

    fun matchNextSpring(spring: Spring): List<SolutionPattern> = buildList {
        if (spring != Spring.Operational) matchDamagedSpring()?.let(::add)
        if (spring != Spring.Damaged) matchOperationalSpring()?.let(::add)
    }

    private fun matchOperationalSpring(): SolutionPattern? = when (constraint) {
        is Constraint.Damaged -> null
        else -> SolutionPattern(Constraint.None, unmatchedDamagedGroups)
    }

    private fun matchDamagedSpring(): SolutionPattern? = when (constraint) {
        Constraint.Operational -> null
        Constraint.None -> startNewDamagedGroup()?.matchDamagedSpring()
        is Constraint.Damaged -> SolutionPattern(constraint.next(), unmatchedDamagedGroups)
    }

    private fun startNewDamagedGroup(): SolutionPattern? =
        if (unmatchedDamagedGroups.isEmpty()) null
        else SolutionPattern(
            Constraint.Damaged(unmatchedDamagedGroups.first()),
            unmatchedDamagedGroups.drop(1)
        )

    fun completeMatch() = !constraint.mandatory && unmatchedDamagedGroups.isEmpty()
}


//Requirements for next springs to match
private sealed class Constraint(val mandatory: Boolean) {

    //no requirements
    data object None : Constraint(false)

    //next spring must be operational
    data object Operational : Constraint(false)

    //next [amount] springs must be damaged, followed by 1 operational spring
    data class Damaged(private val amount: Int) : Constraint(true) {
        fun next() = if (amount > 1) Damaged(amount - 1) else Operational
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
