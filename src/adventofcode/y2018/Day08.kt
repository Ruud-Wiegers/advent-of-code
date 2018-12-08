package adventofcode.y2018

import adventofcode.AdventSolution

object Day08 : AdventSolution(2018, 8, "Memory Maneuver") {

    override fun solvePartOne(input: String) = parse(input).simpleChecksum().toString()

    override fun solvePartTwo(input: String) = parse(input).complexChecksum().toString()

    private fun parse(input: String) = input
            .splitToSequence(" ")
            .map { it.toInt() }
            .let { readNode(it.iterator()) }

    private fun readNode(iter: Iterator<Int>) =
            Node(iter.next(), iter.next()).apply {
                repeat(numChildren) { children += readNode(iter) }
                repeat(numMetadata) { metadata += iter.next() }
            }

    private data class Node(val numChildren: Int, val numMetadata: Int) {
        val children = mutableListOf<Node>()
        val metadata = mutableListOf<Int>()

        fun simpleChecksum(): Int = metadata.sum() + children.sumBy(Node::simpleChecksum)

        fun complexChecksum(): Int = if (children.isEmpty())
            metadata.sum()
        else
            metadata.sumBy { children.getOrNull(it - 1)?.complexChecksum() ?: 0 }
    }
}