package adventofcode.y2020

import adventofcode.io.AdventSolution

fun main() = Day02.solve()

object Day02 : AdventSolution(2020, 2, "Password Philosophy")
{

    override fun solvePartOne(input: String) = input
        .lineSequence()
        .map(::parse)
        .count { (ch, l, h, pwd) -> pwd.count { it == ch } in l..h }

    override fun solvePartTwo(input: String) = input
        .lineSequence()
        .map(::parse)
        .count { (ch, l, h, pwd) -> (pwd[l - 1] == ch) xor (pwd[h - 1] == ch) }

    private val regex = """(\d+)-(\d+) (.): (.+)""".toRegex()
    private fun parse(input: String): Policy
    {
        val (low, high, character, password) = regex.matchEntire(input)!!.destructured
        return Policy(character[0], low.toInt(), high.toInt(), password)
    }

    private data class Policy(val ch: Char, val l: Int, val h: Int, val pwd: String)
}
