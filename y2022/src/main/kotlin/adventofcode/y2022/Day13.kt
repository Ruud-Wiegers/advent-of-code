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

private fun tokenize(input: String) = sequence {
    var remainder = input

    while (remainder.isNotEmpty()) {
        when (remainder[0]) {
            '[' -> yield(Token.Push).also { remainder = remainder.drop(1) }
            ']' -> yield(Token.Pop).also { remainder = remainder.drop(1) }
            ',' -> remainder = remainder.drop(1)
            else -> {
                val s = remainder.takeWhile { it.isDigit() }
                remainder = remainder.drop(s.length)
                yield(Token.Num(s.toInt()))
            }
        }
    }
}

private fun parseToNode(tokens: Sequence<Token>): Node {
    val stack = mutableListOf(Branch())
    tokens.forEach {
        when (it) {
            Token.Push -> stack += Branch()
            Token.Pop -> stack.removeLast().let { node -> stack.last().children += node }
            is Token.Num -> stack.last().children += Leaf(it.n)
        }
    }
    return stack.first().children.first()
}

private sealed class Token {
    object Push : Token()
    object Pop : Token()
    data class Num(val n: Int) : Token()
}

private sealed class Node : Comparable<Node>

private data class Leaf(val i: Int) : Node() {
    override fun compareTo(other: Node): Int = when (other) {
        is Leaf -> compareValues(i, other.i)
        is Branch -> Branch(mutableListOf(this)).compareTo(other)
    }

    override fun toString() = i.toString()
}

private data class Branch(val children: MutableList<Node> = mutableListOf()) : Node() {
    override fun compareTo(other: Node): Int = when (other) {
        is Leaf -> compareTo(Branch(mutableListOf(other)))
        is Branch -> compareList(children, other.children)
    }

    private fun compareList(a: List<Node>, b: List<Node>): Int = when {
        a.isEmpty() && b.isEmpty() -> 0
        a.isEmpty() -> -1
        b.isEmpty() -> 1
        a[0].compareTo(b[0]) != 0 -> a[0].compareTo(b[0])
        else -> compareList(a.drop(1), b.drop(1))
    }

    override fun toString() = children.joinToString(",", "[", "]")
}