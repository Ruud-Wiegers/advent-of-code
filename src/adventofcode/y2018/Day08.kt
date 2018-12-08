package adventofcode.y2018

import adventofcode.AdventSolution

object Day08 : AdventSolution(2018, 8, "Memory Maneuver") {

    override fun solvePartOne(input: String) = parse(input).simpleChecksum().toString()

    override fun solvePartTwo(input: String) = parse(input).complexChecksum().toString()

    private fun parse(input: String) = input
            .splitToSequence(" ")
            .map(String::toInt)
            .iterator()
            .let(::Node)

    private class Node(iter: Iterator<Int>) {
        private val children: List<Node>
        private val metadata: List<Int>

        init {
            val childrenCount = iter.next()
            val metadataCount = iter.next()
            children = List(childrenCount) { Node(iter) }
            metadata = List(metadataCount) { iter.next() }
        }

        fun simpleChecksum(): Int = metadata.sum() + children.sumBy(Node::simpleChecksum)

        fun complexChecksum(): Int = if (children.isEmpty())
            metadata.sum()
        else
            metadata.sumBy { children.getOrNull(it - 1)?.complexChecksum() ?: 0 }
    }
}
