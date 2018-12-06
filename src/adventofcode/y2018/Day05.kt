package adventofcode.y2018

import adventofcode.AdventSolution
import java.util.*

object Day05 : AdventSolution(2018, 5, "Alchemical Reduction") {

    override fun solvePartOne(input: String) = process(input).size.toString()

    override fun solvePartTwo(input: String): String {
        val preProcessed = String(process(input).toCharArray())

        return ('a'..'z')
                .asSequence()
                .map { ch -> preProcessed.filter { it.toLowerCase() != ch } }
                .map { process(it).size }
                .min()
                .toString()
    }

    private fun process(input: String): List<Char> = Stack<Char>().apply {
        for (ch in input)
            if (isEmpty() || ch == peek() || ch.toLowerCase() != peek().toLowerCase())
                push(ch)
            else
                pop()
    }
}