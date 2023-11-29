package adventofcode.y2021

import adventofcode.io.AdventSolution

object Day21 : AdventSolution(2021, 21, "Dirac Dice") {
    override fun solvePartOne(input: String): Int {
        var (p1, p2) = parse(input)
        p1--
        p2--
        var p1Score = 0
        var p2Score = 0
        var dice = 1
        var rolls = 0
        fun next() = dice.also { rolls++;if (++dice > 100) dice = 1 }
        while (p2Score < 1000) {
            p1 += next() + next() + next()
            p1 %= 10
            p1Score += p1 + 1
            p1 = p2.also { p2 = p1 }
            p1Score = p2Score.also { p2Score = p1Score }
        }

        return p1Score * rolls
    }

    override fun solvePartTwo(input: String): Long {
        val universes = mutableMapOf<State, WinCount>()
        val distribution3d3 = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
        fun winnings(s: State): WinCount = universes.getOrPut(s) {
            when {
                s.score1 >= 21 -> WinCount(1, 0)
                s.score2 >= 21 -> WinCount(0, 1)
                else -> distribution3d3.map { (roll, frequency) ->
                    winnings(
                        State(
                            pos1 = s.pos2,
                            pos2 = (s.pos1 + roll) % 10,
                            score1 = s.score2,
                            score2 = s.score1 + (s.pos1 + roll) % 10 + 1
                        )
                    ).flip() * frequency.toLong()
                }
                    .fold(WinCount(0, 0), WinCount::plus)
            }
        }

        val (p1, p2) = parse(input)
        val (p1Wins, p2Wins) = winnings(State(p1 - 1, p2 - 1, 0, 0))
        return maxOf(p1Wins, p2Wins)
    }

    private fun parse(input: String) = input.lines().map { it.last() - '0' }

    private data class WinCount(val p1: Long, val p2: Long) {
        operator fun plus(o: WinCount) = WinCount(p1 + o.p1, p2 + o.p2)
        operator fun times(s: Long) = WinCount(p1 * s, p2 * s)
        fun flip() = WinCount(p2, p1)
    }

    private data class State(val pos1: Int, val pos2: Int, val score1: Int, val score2: Int)
}