package adventofcode.y2024

import adventofcode.io.AdventSolution

fun main() {
    Day09.solve()
}

object Day09 : AdventSolution(2024, 9, "Resonant Collinearity") {

    override fun solvePartOne(input: String): Long {
        val inputSize = (input.length + 1) / 2L

        val f = Reader(false, -1, 0, -1, input)
        val b = Reader(false, inputSize, 0, input.length, input)

        return combinedRead(f, b).withIndex().sumOf { (i, v) -> i * v }
    }


    data class Reader(
        var isReadingData: Boolean,
        var currentId: Long,
        var cellsRemaining: Int,

        var position: Int,
        val data: String
    ) {

        fun readForward(): Long? {
            while (cellsRemaining == 0) {
                position++
                if (position >= data.length) return null
                isReadingData = !isReadingData
                if (isReadingData) currentId++
                cellsRemaining = data[position].digitToInt()
            }

            cellsRemaining--

            return if (isReadingData) currentId else -1
        }

        fun readBackward(): Long? {
            while (cellsRemaining == 0) {
                position--
                if (position < 0) return null
                isReadingData = !isReadingData
                if (isReadingData) currentId--
                cellsRemaining = data[position].digitToInt()
            }

            cellsRemaining--

            return if (isReadingData) currentId else -1
        }

    }

    fun combinedRead(f: Reader, b: Reader): Sequence<Long> = generateSequence {
        if (f.position > b.position) null //TODO if reading same data block, finish data block

        else if (f.position == b.position && f.cellsRemaining + b.cellsRemaining <= f.data[f.position].digitToInt()) null
        else {

            //TODO fix end bit, there's still the possibility that the fwd reader is in an empty block and the bwd reader tries to read beyond that block, giving a doubled read?

            val fwd = f.readForward()!!

            if (fwd >= 0) fwd else {
                var bwd = b.readBackward()
                while (bwd == -1L)
                    bwd = b.readBackward()

                bwd
            }
        }
    }


    override fun solvePartTwo(input: String): Long {

        val chunks = input.scan(0L) { acc, s -> acc + s.digitToInt() }.zipWithNext { a, b -> a until b }.chunked(2)


        fun LongRange.size() = last - first + 1

        val files = chunks.map { it[0] }.withIndex()
        val gaps = chunks.mapNotNull { if (it.size == 2) it[1] else null }.toMutableList()

        val movedFiles = files.reversed().map { file ->

            val gap = gaps.firstOrNull { gap -> gap.last < file.value.first && file.value.size() <= gap.size() }

            if (gap == null) {
                file
            } else {
                val movedFile = gap.first until gap.first + file.value.size()
                val i = gaps.indexOf(gap)
                gaps[i] = gap.first + file.value.size()..gap.last()
                file.copy(value = movedFile)
            }
        }
        return movedFiles.sumOf { (i, v) -> v.sumOf(i::times) }
    }
}




