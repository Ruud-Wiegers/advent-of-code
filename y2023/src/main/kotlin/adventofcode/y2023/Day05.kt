package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day05.solve()
}

object Day05 : AdventSolution(2023, 5, "If You Give A Seed A Fertilizer") {

    override fun solvePartOne(input: String): Long? {
        val (seeds, converters) = parse(input)

        return seeds.minOfOrNull { seed ->
            converters.fold(seed) { category, converter -> converter.shift(category) }
        }
    }

    override fun solvePartTwo(input: String): Long {
        val (seeds, converters) = parse(input)

        val seedRanges = seeds.chunked(2).map { (start, length) -> start until start + length }

        return converters
            .fold(seedRanges) { categoryRanges, converter ->
                converter.cut(categoryRanges).map { categoryRange -> converter.shiftRange(categoryRange) }
            }
            .minOf { it.first }
    }
}

private fun parse(input: String): Pair<List<Long>, List<Converter>> {
    val sections = input.split("\n\n")

    val seeds = sections[0].substringAfter("seeds: ").split(" ").map(String::toLong)

    val converters = sections.drop(1).map { section ->
        section.lines().drop(1)
            .map { line ->
                val (dest, source, length) = line.split(" ").map(String::toLong)
                Shifter(source until source + length, dest - source)
            }
            .let(::Converter)
    }

    return Pair(seeds, converters)

}


private data class Converter(val ranges: List<Shifter>) {

    //cut up the set of intervals such that each interval is entirely inside or outside a shifter
    fun cut(categoryRanges: List<LongRange>) = ranges.fold(categoryRanges) { range, shifter ->
        range.flatMap { it.cutWith(shifter.range) }
    }

    fun shift(input: Long) = input + (ranges.find { input in it.range }?.delta ?: 0L)
    fun shiftRange(input: LongRange) = shift(input.first)..shift(input.last)
}

private data class Shifter(val range: LongRange, val delta: Long)


private fun LongRange.cutWith(interval: LongRange): List<LongRange> {

    val before = first..last.coerceAtMost(interval.first - 1)
    val inside = first.coerceAtLeast(interval.first)..last.coerceAtMost(interval.last)
    val after = first.coerceAtLeast(interval.last + 1)..last

    return listOf(before, inside, after).filterNot(LongRange::isEmpty)
}