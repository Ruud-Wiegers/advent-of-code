package adventofcode.y2016

import adventofcode.AdventSolution

object Day16 : AdventSolution(2016, 16, "Dragon Checksum ") {
    override fun solvePartOne(input: String) = fillDisk(input, 272).checksum()
    override fun solvePartTwo(input: String) = fillDisk(input, 35651584).checksum()
}

private fun fillDisk(inputValue: String, size: Int): BooleanArray {
    val initial = BooleanArray(inputValue.length) { inputValue[it] == '1' }
    val disk = generateSequence(initial, BooleanArray::dragon)
            .first { it.size >= size }

    return disk.copyOf(size)
}

private fun BooleanArray.dragon(): BooleanArray {
    val doubled = this.copyOf(this.size * 2 + 1)
    for (i in indices) doubled[doubled.lastIndex - i] = !this[i]
    return doubled
}

private fun BooleanArray.checksum(): String =
        generateSequence(this) { it.diff() }
                .first { it.size % 2 != 0 }
                .map { if (it) '1' else '0' }
                .fold(StringBuilder(), StringBuilder::append)
                .toString()

private fun BooleanArray.diff() = BooleanArray(this.size / 2) { this[it * 2] == this[it * 2 + 1] }
