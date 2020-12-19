package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day19.solve()

object Day19 : AdventSolution(2020, 19, "Monster Messages")
{
    override fun solvePartOne(input: String): Int
    {
        val (rulesInput, messages) = input.split("\n\n")

        val rules: List<Rule> = parseRules(rulesInput)

        return messages.lines().count { matches(it, listOf(0), rules) }
    }

    override fun solvePartTwo(input: String): Int
    {
        val (rulesInput, messages) = input.split("\n\n")

        val rules: List<Rule> = parseRules(rulesInput).toMutableList().apply {

            //the first index in each branch (42) will always produce terminals, so the algorithm will always terminate
            set(8, parseRule("42 | 42 8"))
            set(11, parseRule("42 31 | 42 11 31"))
        }

        return messages.lines().count { matches(it, listOf(0), rules) }
    }

    private fun parseRules(input: String): List<Rule> = input.lines()
        .map { it.split(":") }
        .sortedBy { it[0].toInt() }
        .map { parseRule(it[1].trim()) }

    private fun parseRule(input: String): Rule = if (input.startsWith('"')) Rule.Literal(input[1])
    else input.split("|").map { it.split(" ").filter(String::isNotBlank).map(String::toInt) }.let(Rule::Split)

    private fun matches(remainder: String, unmatchedRules: List<Int>, rules: List<Rule>): Boolean
    {
        val rule: Rule? = unmatchedRules.firstOrNull()?.let { rules[it] }
        return when
        {
            remainder.isEmpty()                               -> rule == null
            rule == null                                      -> false
            rule is Rule.Literal && rule.ch != (remainder[0]) -> false
            rule is Rule.Literal                              -> matches(remainder.drop(1), unmatchedRules.drop(1), rules)
            rule is Rule.Split                                -> rule.alternatives.any { alt ->
                matches(remainder, alt + unmatchedRules.drop(1), rules)
            }
            else                                              -> false
        }
    }

    private sealed class Rule
    {
        data class Literal(val ch: Char) : Rule()
        data class Split(val alternatives: List<List<Int>>) : Rule()
    }
}