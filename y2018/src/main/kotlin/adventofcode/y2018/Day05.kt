package adventofcode.y2018

import adventofcode.AdventSolution
import java.util.*

object Day05 : AdventSolution(2018, 5, "Alchemical Reduction") {

    override fun solvePartOne(input: String) = process(input).size

    override fun solvePartTwo(input: String): Int? {
        val preProcessed = String(process(input).toCharArray())

        return ('a'..'z')
            .asSequence()
            .map { ch -> preProcessed.filter { it.toLowerCase() != ch } }
            .map { process(it).size }
            .minOrNull()

    }

    private fun process(input: String): Deque<Char> = ArrayDeque<Char>().apply {
        for (ch in input)
            if (isEmpty() || ch == peek() || ch.toLowerCase() != peek().toLowerCase())
                push(ch)
            else
                pop()
    }
}
