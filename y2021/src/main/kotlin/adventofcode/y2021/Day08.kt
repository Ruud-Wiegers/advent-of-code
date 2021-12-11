package adventofcode.y2021

import adventofcode.AdventSolution


object Day08 : AdventSolution(2021, 8, "Seven Segment Search") {
    override fun solvePartOne(input: String) =
        parseInput(input).flatMap(DecodingProblem::message).count { it.size in listOf(2, 3, 4, 7) }

    override fun solvePartTwo(input: String) = parseInput(input).sumOf { (digits, message) ->
        val patterns = digits.sortDigitPatterns()
        message.map(patterns::indexOf).joinToString("").toInt()
    }

    private fun parseInput(input: String) = input.lineSequence().map {
        val (digits, message) = it.split(" | ").map { list -> list.split(" ").map(String::toSet) }
        DecodingProblem(digits, message)
    }

    private fun Collection<Set<Char>>.sortDigitPatterns(): List<SegmentEncodedDigit> {
        val one = single { it.size == 2 }
        val four = single { it.size == 4 }
        val seven = single { it.size == 3 }
        val eight = single { it.size == 7 }

        val bottomLeftSegment = ('a'..'g').single { segment -> count { segment in it } == 4 }

        val fiveSegments = filter { it.size == 5 }
        val two = fiveSegments.single { bottomLeftSegment in it }
        val three = fiveSegments.single { (it intersect one).size == 2 }
        val five = fiveSegments.single { it != two && it != three }

        val sixSegments = filter { it.size == 6 }
        val six = sixSegments.single { (it intersect one).size == 1 }
        val nine = sixSegments.single { bottomLeftSegment !in it }
        val zero = sixSegments.single { it != six && it != nine }

        return listOf(zero, one, two, three, four, five, six, seven, eight, nine)
    }
}

private data class DecodingProblem(val digitPatterns: List<SegmentEncodedDigit>, val message: List<SegmentEncodedDigit>)
private typealias SegmentEncodedDigit = Set<Char>