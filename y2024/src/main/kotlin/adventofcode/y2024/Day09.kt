package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day09.solve()
}

object Day09 : AdventSolution(2024, 9, "Resonant Collinearity") {

    override fun solvePartOne(input: String): Long {

        val disk = input.chunked(2).flatMapIndexed { index, str ->
            List(str[0].digitToInt()) { index } + List(str.getOrElse(1) { '0' }.digitToInt()) { -1 }
        }.toIntArray()

        var f = 0
        var b = disk.lastIndex

        val result = buildList {
            while (f <= b) {
                val forward = disk[f++]
                if (forward >= 0)
                    add(forward)
                else {
                    var backward: Int
                    do
                        backward = disk[b--]
                    while (backward == -1)
                    add(backward)
                }
            }
        }

        return result.mapIndexed(Int::times).sumOf(Int::toLong)
    }


    override fun solvePartTwo(input: String): Long {
        val (files, gaps) = parseInput(input)

        val movedFiles = files.reversed().map { file ->


            val gapIndex = gaps.indexOfFirst { gap -> gap.last < file.value.first && file.value.size() <= gap.size() }

            if (gapIndex == -1) {
                file
            } else {
                val gap = gaps[gapIndex]
                val movedFile = gap.first until gap.first + file.value.size()
                gaps[gapIndex] = gap.first + file.value.size()..gap.last()
                file.copy(value = movedFile)
            }
        }

        fun LongRange.sum(): Long = (first + last) * size() / 2

        return movedFiles.sumOf { (i, v) -> i * v.sum() }
    }
}

private fun parseInput(input: String): Pair<Iterable<IndexedValue<LongRange>>, MutableList<LongRange>> {
    val ranges =
        input.asSequence().map(Char::digitToInt).scan(0L, Long::plus).zipWithNext(Long::rangeUntil).toList()

    val files = ranges.filterIndexed { i, v -> i % 2 == 0 }.withIndex()
    val gaps = ranges.filterIndexed { i, v -> i % 2 != 0 }.toMutableList()
    return Pair(files, gaps)
}

private fun LongRange.size() = last - first + 1



