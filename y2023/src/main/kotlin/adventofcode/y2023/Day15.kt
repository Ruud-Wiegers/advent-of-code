package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve

fun main() {
    Day15.solve()
}

object Day15 : AdventSolution(2023, 15, " Lens Library") {

    override fun solvePartOne(input: String) = input.splitToSequence(",").sumOf(::hash)

    override fun solvePartTwo(input: String): Int {
        val hashmap = List(256) { mutableListOf<Lens>() }

        input.splitToSequence(",").forEach { instruction ->
            val label = instruction.dropLast(if (instruction.last() == '-') 1 else 2)
            val focalLength = instruction.last().digitToIntOrNull()

            val box = hashmap[hash(label)]
            val index = box.indexOfFirst { it.label == label }

            when {
                focalLength != null && index >= 0 -> box[index] = Lens(label, focalLength)
                focalLength != null -> box += Lens(label, focalLength)
                index >= 0 -> box.removeAt(index)
            }

        }

        return hashmap.flatMapIndexed { box, content ->
            content.mapIndexed { index, lens -> (box + 1) * (index + 1) * lens.focalLength }
        }.sum()
    }
}

private fun hash(label: String) = label.fold(0) { acc, ch -> (acc + ch.code) * 17 % 256 }


private data class Lens(val label: String, val focalLength: Int)
