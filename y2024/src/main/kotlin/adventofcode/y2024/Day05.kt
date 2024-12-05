package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day05.solve()
}

object Day05 : AdventSolution(2024, 5, "Print Queue") {

    override fun solvePartOne(input: String): Int {
        val (rules, updates) = parseInput(input)

        return updates
            .filter { update -> rules.all(update::conformsToRule) }
            .sumOf { it.middle() }
    }

    override fun solvePartTwo(input: String): Int {
        val (rules, updates) = parseInput(input)

        return updates
            .filterNot { update -> rules.all(update::conformsToRule) }
            .map { update -> reorderedUpdate(update, rules) }
            .sumOf { it.middle() }
    }
}

private fun parseInput(input: String): Pair<List<List<Int>>, List<List<Int>>> {
    val (rulesInput, pagesInput) = input.split("\n\n")

    val rules = rulesInput.lines().map { it.split("|").map(String::toInt) }
    val pages = pagesInput.lines().map { it.split(",").map(String::toInt) }

    return rules to pages
}

private fun List<Int>.conformsToRule(rule: List<Int>): Boolean {
    val iBefore = indexOf(rule.first())
    val iAfter = indexOf(rule.last())
    return iAfter == -1 || iBefore < iAfter
}

private fun reorderedUpdate(update: List<Int>, rules: List<List<Int>>): List<Int> {
    //keep ony rules that order pages in this update
    val relevantRules: List<List<Int>> = rules.filter { it.all(update::contains) }

    //if there's no way to order the remaining pages, just return them
    if(relevantRules.isEmpty()) return update

    //find a page that is not _before_ any other page according to the rules
    val lastPage = update.first { page -> relevantRules.none { rule -> rule.first() == page } }

    //stick that page on the end of the reordered list, solve subproblem
    return reorderedUpdate(update - lastPage, relevantRules) + lastPage
}

private fun List<Int>.middle() = this[this.size / 2]