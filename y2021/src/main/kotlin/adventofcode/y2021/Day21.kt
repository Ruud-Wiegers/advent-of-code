package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main() {
    Day21.solve()
}

object Day21 : AdventSolution(2021, 21, "???") {
    override fun solvePartOne(input: String): Int {
        var (p1, p2) = parse(input)

        p1--
        p2--

        var p1Score = 0
        var p2Score = 0

        var dice = 1
        var rolls = 0

        fun next() = dice.also { rolls++;if (++dice > 100) dice = 1 }
        while (p1Score < 1000 && p2Score < 1000) {
            p1 += next() + next() + next()
            p1 %= 10
            p1Score += p1 + 1
            p1 = p2.also { p2 = p1 }
            p1Score = p2Score.also { p2Score = p1Score }
        }

        return minOf(p1Score, p2Score) * rolls
    }

    override fun solvePartTwo(input: String): Long {
        val (p1, p2) = parse(input)
        val initial = State(p1 - 1, p2 - 1, 0, 0)

        var universes = mapOf(initial to 1L)
        var p1Wins = 0L
        var p2Wins = 0L

        var p1Turn = true

        fun roll() = universes.asSequence().flatMap { (oldState, count) ->
            (1..3).map { roll ->
                if (p1Turn) oldState.copy(pos1 = (oldState.pos1 + roll) % 10) to count
                else oldState.copy(pos2 = (oldState.pos2 + roll) % 10) to count
            }
        }.groupBy({ it.first }, { it.second })
            .mapValues { it.value.sum() }

        fun score() = universes.mapKeys { (oldState) ->
            if (p1Turn) oldState.copy(score1 = oldState.score1 + oldState.pos1+1)
            else oldState.copy(score2 = oldState.score2 + oldState.pos2+1)

        }

        while (universes.isNotEmpty()) {
            universes = roll()
            universes = roll()
            universes = roll()
            universes = score()

            p1Wins += universes.filterKeys { it.score1 >= 21 }.values.sum()
            p2Wins += universes.filterKeys { it.score2 >= 21 }.values.sum()
            universes = universes.filterKeys { it.score1 <21 && it.score2 <21 }

            p1Turn = !p1Turn
        }

        return maxOf(p1Wins,p2Wins)
    }

    private fun parse(input: String) = input.lines().map { it.last() - '0' }

    private data class State(val pos1: Int, val pos2: Int, val score1: Int, val score2: Int)


}