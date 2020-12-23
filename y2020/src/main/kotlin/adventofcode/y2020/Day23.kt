package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day23.solve()

object Day23 : AdventSolution(2020, 23, "Crab Cups")
{
    override fun solvePartOne(input: String): Any
    {
        val circle = CupCircle(parse(input))
        repeat(100) { circle.shuffle() }
        return circle.asSequence().map { it.label }.joinToString("")
    }

    override fun solvePartTwo(input: String): Any
    {
        return solveBetter(parse(input))
        /*
        val circle = CupCircle(parse(input))
        circle.addAll(10..1_000_000)
        repeat(10_000_000) { circle.shuffle() }
        return circle.asSequence().take(2).map { it.label.toLong() }.reduce(Long::times)
        */
    }

    private fun parse(input: String) = input.map(Char::toString).map(String::toInt)

    fun solveBetter(i: List<Int>): Long
    {
        val nextCup = IntArray(1_000_001) { it + 1 }

        nextCup[nextCup.lastIndex] = i[0]
        i.zipWithNext().forEach { (c, n) -> nextCup[c] = n }
        nextCup[i.last()] = i.size + 1

        var current = i[0]
        repeat(10_000_000) {

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

        return nextCup[1].toLong() * nextCup[nextCup[1]].toLong()
    }

    class CupCircle(labels: List<Int>)
    {
        private val one: Cup
        private var currentCup: Cup

        init
        {
            val cups = labels.associateWith { Cup(it) }
            one = cups[1]!!
            currentCup = cups.getValue(labels.first())

            //link each cup to the next in the circle
            val circlePairs = labels.zipWithNext() + (labels.last() to labels.first())
            for ((c, n) in circlePairs)
            {
                cups.getValue(c).next = cups.getValue(n)
            }

            //link each cup to the previous in value
            for ((id, cup) in cups)
            {
                cup.lower = cups.getValue(if (id == 1) labels.maxOrNull()!! else id - 1)
            }
        }

        fun addAll(labels: IntRange)
        {
            val cups = labels.map(::Cup)
            //link the list of new cups together
            cups.zipWithNext().forEach { (a, b) ->
                a.next = b
                b.lower = a
            }

            //Insert into the circle:

            //fix references to previous label
            cups.first().lower = one.lower
            one.lower = cups.last()

            //Fix references to next element
            asSequence().first { it.next == currentCup }.next = cups.first()
            cups.last().next = currentCup
        }

        fun shuffle()
        {
            //grab the 3 cups to move
            val toMove = listOf(currentCup.next, currentCup.next.next, currentCup.next.next.next)

            //disconnect from current location
            currentCup.next = toMove.last().next

            //find new location
            var target = currentCup.lower
            while (target in toMove) target = target.lower

            //insert into new location
            toMove.last().next = target.next
            target.next = toMove.first()

            //advance current cup
            currentCup = currentCup.next
        }

        fun asSequence() = generateSequence(one.next, Cup::next).takeWhile { it != one }

        class Cup(val label: Int)
        {
            lateinit var next: Cup
            lateinit var lower: Cup
        }
    }
}