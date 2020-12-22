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
        return result.reversed().withIndex().map { (i, v) -> (i + 1L) * v }.reduce(Long::plus)
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

    fun runGame(deckA: List<Int>, deckB: List<Int>): Pair<Boolean, List<Int>>
    {
        val seen = mutableSetOf<Pair<List<Int>, List<Int>>>()

        var da = deckA
        var db = deckB

        while (true)
        {
            when
            {
                da.isEmpty()        -> return false to db
                db.isEmpty()        -> return true to da
                da to db in seen -> return true to da
            }
            seen += da to db

            val a = da.first()
            val b = db.first()

            val round = if (da.size > a && db.size > b)
                runGame(da.subList(1, a+1), db.subList(1, b+1)).first
            else a > b

            if (round)
            {
                da = da.drop(1) + a + b
                db = db.drop(1)
            }
            else
            {
                da = da.drop(1)
                db = db.drop(1) + b + a
            }
        }
    }

    override fun solvePartTwo(input: String): Any
    {
        val (a, b) = parse(input)
        val combat = runGame(a, b)


        return combat.second.reversed().withIndex().map { (i, v) -> (i + 1L) * v }.reduce(Long::plus)
    }
}
