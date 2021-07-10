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

            //the first index in each branch (42) will always produce terminals,
            //so 'looping' will consume part of the message
            //so the algorithm will always terminate
            set(8, parseRule("42 | 42 8"))
            set(11, parseRule("42 31 | 42 11 31"))
        }

        return messages.lines().count { matches(it, listOf(0), rules) }
    }

    private fun parseRules(input: String): List<Rule> = input.lines()
        .map { it.split(": ") }
        .sortedBy { it[0].toInt() }
        .map { parseRule(it[1]) }

    private fun parseRule(input: String): Rule = if (input.startsWith('"'))
        Rule.Terminal(input[1])
    else
        input.split(" | ").map { it.split(" ").map(String::toInt) }.let(Rule::Dnf)

    private fun matches(unmatchedMessage: String, unmatchedRules: List<Int>, grammar: List<Rule>): Boolean =
        when (val rule = unmatchedRules.firstOrNull()?.let(grammar::get))
        {
            null             -> unmatchedMessage.isEmpty()
            is Rule.Terminal -> rule.ch == unmatchedMessage.firstOrNull() && matches(unmatchedMessage.drop(1), unmatchedRules.drop(1), grammar)
            is Rule.Dnf      -> rule.alternatives.any { alt -> matches(unmatchedMessage, alt + unmatchedRules.drop(1), grammar) }
        }

    private sealed class Rule
    {
        data class Terminal(val ch: Char) : Rule()
        data class Dnf(val alternatives: List<List<Int>>) : Rule()
    }
}