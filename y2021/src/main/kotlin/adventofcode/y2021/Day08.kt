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
        .flatMap { it.message }.count { it.size in listOf(2, 3, 4, 7) }

    override fun solvePartTwo(input: String): Int
    {
        return parseInput(input).sumOf { it.solve() }
    }

    private fun parseInput(input: String): List<DecodingProblem>
    {
        return input.lines().map {
            val (digits, message) = it.split(" | ")
            DecodingProblem(
                digits.split(" ").map { it.toSortedSet() },
                message.split(" ").map { it.toSortedSet() },
            )
        }
    }
}

private fun DecodingProblem.solve(): Int
{
    val one = digits.first { it.size == 2 }
    val four = digits.first { it.size == 4 }
    val seven = digits.first { it.size == 3 }
    val eight = digits.first { it.size == 7 }

    val bottomLeftSegment = ('a'..'g').first { segment -> digits.count { segment in it } == 4 }

    val two = digits.first { it.size == 5 && bottomLeftSegment in it }
    val three = digits.first { it.size == 5 && (it intersect one).size == 2 }
    val five = digits.first { it.size == 5 && it != two && it != three }

    val six = digits.first { it.size == 6 && (it intersect one).size == 1 }
    val nine = digits.first { it.size == 6 && bottomLeftSegment !in it }
    val zero = digits.first { it.size == 6 && it != six && it != nine }

    val map = listOf(zero, one, two, three, four, five, six, seven, eight, nine)

    return message.map(map::indexOf).joinToString("").toInt()
}

private data class DecodingProblem(val digits: List<EncodedDigit>, val message: List<EncodedDigit>)

private typealias EncodedDigit = Set<Char>