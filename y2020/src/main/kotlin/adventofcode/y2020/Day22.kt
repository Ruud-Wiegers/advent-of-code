package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day22.solve()

object Day22 : AdventSolution(2020, 22, "Crab Combat")
{
    override fun solvePartOne(input: String): Long
    {
        val combat = generateSequence(parse(input)) { round(it.first, it.second) }
            .first { it.first.isEmpty() || it.second.isEmpty() }

        val result = (combat.first + combat.second)
        return result.reversed().mapIndexed { i, v -> (i + 1L) * v }.reduce(Long::plus)
    }

    fun parse(input: String): Pair<List<Int>, List<Int>>
    {
        val (a, b) = input.split("\n\n")

        val al = a.lines().drop(1).map(String::toInt)
        val bl = b.lines().drop(1).map(String::toInt)
        return Pair(al, bl)
    }

    fun round(a: List<Int>, b: List<Int>): Pair<List<Int>, List<Int>>
    {
        val (wa, wb) = a.zip(b).partition { (a, b) -> a > b }

        val ra = a.drop(b.size) + wa.flatMap { listOf(it.first, it.second) }
        val rb = b.drop(a.size) + wb.flatMap { listOf(it.second, it.first) }
        return Pair(ra, rb)
    }

    fun runGame(deckA: List<Int>, deckB:List<Int>): List<List<Int>>
    {
        val seen = mutableSetOf<Int>()

        var d = listOf(ArrayDeque(deckA), ArrayDeque(deckB))

        while (d.none { it.isEmpty() })
        {
            val hashCode = d.hashCode()
            if( hashCode in seen) break
            seen += hashCode

            val tops = d.map { it.removeAt(0) }

            val p1Wins = if (d[0].size >= tops[0] && d[1].size >= tops[1] )

                runGame(d[0].subList(0, tops[0]),d[1].subList(0,tops[1]) )[0].isNotEmpty()
            else
                tops[0]>tops[1]


            if(p1Wins)
                d[0].addAll(tops)
            else d[1].addAll(tops.reversed())

        }
        return d
    }

    override fun solvePartTwo(input: String): Any
    {
        val (a, b) = parse(input)
        val combat = runGame(a, b)

        return combat.first { it.isNotEmpty() }.reversed().mapIndexed { i, v -> (i + 1L) * v }.reduce(Long::plus)
    }
}
