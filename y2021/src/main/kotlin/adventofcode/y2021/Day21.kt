package adventofcode.y2021

import adventofcode.AdventSolution

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
        val (p1, p2) = parse(input)
        val initial = State(p1 - 1, p2 - 1, 0, 0)

        var universes = mapOf(initial to 1L)
        var p1Wins = 0L
        var p2Wins = 0L

        var p1Turn = true
        val distribution3d3 = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
        fun Map<State, Long>.takeTurn(isTurnForPlayer1: Boolean): Map<State, Long> {

            return asSequence()
                .flatMap { (oldState, count) ->
                    distribution3d3.map { (rv, rc) ->
                        if (isTurnForPlayer1) {
                            val newPos = (oldState.pos1 + rv) % 10
                            val newScore = oldState.score1 + newPos + 1
                            oldState.copy(pos1 = newPos, score1 = newScore) to count * rc
                        } else {
                            val newPos = (oldState.pos2 + rv) % 10
                            val newScore = oldState.score2 + newPos + 1
                            oldState.copy(pos2 = newPos, score2 = newScore) to count * rc
                        }
                    }
                }
                .groupingBy { it.first }
                .fold(0) { acc, (_, v) -> acc + v }
        }

        val winningScore = 21
        while (universes.isNotEmpty()) {
            universes = universes.takeTurn(p1Turn)
            p1Wins += universes.filterKeys { it.score1 >= winningScore }.values.sum()
            p2Wins += universes.filterKeys { it.score2 >= winningScore }.values.sum()
            universes = universes.filterKeys { it.score1 < winningScore && it.score2 < winningScore }
            p1Turn = !p1Turn
        }

        return maxOf(p1Wins, p2Wins)
    }

    private fun parse(input: String) = input.lines().map { it.last() - '0' }

    private data class State(val pos1: Int, val pos2: Int, val score1: Int, val score2: Int)
}