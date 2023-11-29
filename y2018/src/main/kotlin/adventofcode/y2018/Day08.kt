package adventofcode.y2018

import adventofcode.io.AdventSolution

object Day08 : AdventSolution(2018, 8, "Memory Maneuver") {

    override fun solvePartOne(input: String) = parseToIterator(input).parseToTree().simpleChecksum()

    override fun solvePartTwo(input: String) = parseToIterator(input).parseToTree().complexChecksum()

    private fun parseToIterator(input: String) = input.splitToSequence(' ').map(String::toInt).iterator()

    private fun Iterator<Int>.parseToTree(): Node {
        val childrenCount = next()
        val metadataCount = next()
        return Node(
                List(childrenCount) { parseToTree() },
                List(metadataCount) { next() }
        )
    }

    private data class Node(private val children: List<Node>, private val metadata: List<Int>) {

        fun simpleChecksum(): Int = metadata.sum() + children.sumOf(Node::simpleChecksum)

        fun complexChecksum(): Int = if (children.isEmpty())
            metadata.sum()
        else
            metadata.sumOf { children.getOrNull(it - 1)?.complexChecksum() ?: 0 }
    }
}
