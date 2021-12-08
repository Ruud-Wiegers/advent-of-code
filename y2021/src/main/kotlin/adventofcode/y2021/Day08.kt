package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main()
{
    Day08.solve()
}

object Day08 : AdventSolution(2021, 8, "???")
{
    override fun solvePartOne(input: String) = parseInput(input)
        .flatMap { it.message }
        .count { it.size in listOf(2, 3, 4, 7) }

    override fun solvePartTwo(input: String) = parseInput(input).sumOf { (digits, message) ->
        val patterns = digits.digitPatterns()
        message.map(patterns::indexOf).joinToString("").toInt()
    }

    private fun parseInput(input: String) = input.lineSequence()
        .map {
            val (digits, message) = it.split(" | ")
            DecodingProblem(
                digits.split(" ").map(String::toSet),
                message.split(" ").map(String::toSet)
            )
        }

    private fun List<Set<Char>>.digitPatterns(): List<Set<Char>>
    {
        val one = first { it.size == 2 }
        val four = first { it.size == 4 }
        val seven = first { it.size == 3 }
        val eight = first { it.size == 7 }

        val bottomLeftSegment = ('a'..'g').first { segment -> count { segment in it } == 4 }

        val two = first { it.size == 5 && bottomLeftSegment in it }
        val three = first { it.size == 5 && (it intersect one).size == 2 }
        val five = first { it.size == 5 && it != two && it != three }

        val six = first { it.size == 6 && (it intersect one).size == 1 }
        val nine = first { it.size == 6 && bottomLeftSegment !in it }
        val zero = first { it.size == 6 && it != six && it != nine }

        return listOf(zero, one, two, three, four, five, six, seven, eight, nine)
    }

    private data class DecodingProblem(val digits: List<Set<Char>>, val message: List<Set<Char>>)
}