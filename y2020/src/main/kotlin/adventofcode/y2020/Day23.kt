package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day23.solve()

object Day23 : AdventSolution(2020, 23, "Crab Cups")
{
    override fun solvePartOne(input: String): Any
    {
        val nextCup = shuffle(parse(input), 9, 100)
        return generateSequence(1) { nextCup[it] }.drop(1).takeWhile { it != 1 }.joinToString("")
    }

    override fun solvePartTwo(input: String): Any
    {
        val nextCup = shuffle(parse(input), 1_000_000, 10_000_000)
        return nextCup[1].toLong() * nextCup[nextCup[1]].toLong()
    }

    private fun parse(input: String) = input.map(Char::toString).map(String::toInt)

    private fun shuffle(input: List<Int>, size: Int, steps: Int): IntArray
    {
        val nextCup = IntArray(size + 1) { it + 1 }

        nextCup[nextCup.lastIndex] = input[0]
        for ((c, n) in input.zipWithNext()) nextCup[c] = n

        nextCup[input.last()] = if (nextCup.lastIndex> input.size) input.size + 1 else input.first()

        var current = input[0]

        repeat(steps) {
            val a = nextCup[current]
            val b = nextCup[a]
            val c = nextCup[b]
            nextCup[current] = nextCup[c]
            fun lower(i: Int) = if (i == 1) nextCup.lastIndex else i - 1
            var target = lower(current)
            while (target == a || target == b || target == c) target = lower(target)
            nextCup[c] = nextCup[target]
            nextCup[target] = a
            current = nextCup[current]
        }

        return nextCup
    }
}