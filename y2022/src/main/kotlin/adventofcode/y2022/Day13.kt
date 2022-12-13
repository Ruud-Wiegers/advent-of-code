package adventofcode.y2022

import adventofcode.AdventSolution

object Day13 : AdventSolution(2022, 13, "Distress Signal") {

    override fun solvePartOne(input: String) = parse(input)
        .chunked(2)
        .withIndex()
        .filter { (_, v) -> v[0] < v[1] }
        .sumOf { it.index + 1 }

    override fun solvePartTwo(input: String): Int {
        val dividers = listOf("[[2]]", "[[6]]")
        return (dividers + input).joinToString("\n")
            .let(::parse)
            .sorted()
            .withIndex()
            .filter { it.value.toString() in dividers }
            .map { it.index + 1 }
            .reduce(Int::times)
    }
}

private fun parse(input: String): Sequence<Node> = input.lineSequence()
    .filter(String::isNotEmpty)
    .map(::tokenize)
    .map(::parseToNode)

private fun tokenize(input: String) = iterator {
    var remainder = input

    while (remainder.isNotEmpty()) {
        when (remainder[0]) {
            '[' -> yield("[").also { remainder = remainder.drop(1) }
            ']' -> yield("]").also { remainder = remainder.drop(1) }
            ',' -> remainder = remainder.drop(1)
            else -> {
                val s = remainder.takeWhile { it.isDigit() }
                remainder = remainder.drop(s.length)
                yield(s)
            }
        }
    }
}

private fun parseToNode(tokens: Iterator<String>): Node {
    val nodes = mutableListOf<Node>()
    while (tokens.hasNext()) {
        when (val t = tokens.next()) {
            "[" -> nodes += parseToNode(tokens)
            "]" -> return Branch(nodes)
            else -> nodes += Leaf(t.toInt())
        }
    }
    return nodes.single()
}

private sealed class Node : Comparable<Node>

private data class Leaf(val i: Int) : Node() {
    override fun compareTo(other: Node): Int = when (other) {
        is Leaf -> i.compareTo(other.i)
        is Branch -> Branch(listOf(this)).compareTo(other)
    }

    override fun toString() = i.toString()
}

private data class Branch(val children: List<Node>) : Node() {
    override fun compareTo(other: Node): Int = when (other) {
        is Leaf -> compareTo(Branch(mutableListOf(other)))
        is Branch -> children.zip(other.children, Node::compareTo)
            .find { it != 0 } ?: children.size.compareTo(other.children.size)
    }

    override fun toString() = children.joinToString(",", "[", "]")
}