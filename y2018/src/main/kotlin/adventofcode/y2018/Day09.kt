package adventofcode.y2018

import adventofcode.io.AdventSolution
import java.util.*

object Day09 : AdventSolution(2018, 9, "Marble Mania") {

    override fun solvePartOne(input: String) = parse(input).let { (p, m) -> game(p, m) }

    override fun solvePartTwo(input: String) = parse(input).let { (p, m) -> game(p, m * 100) }

    //The magic trick: use a deque, also rotate the circle, not a cursor
    private fun game(players: Int, highestMarble: Int): Long? {
        val scores = LongArray(players)
        val circle = ArrayDeque<Int>(highestMarble)
        circle += 0

        for (nextMarble in 1..highestMarble) {
            if (nextMarble % 23 == 0) {
                repeat(7) { circle.offerFirst(circle.pollLast()) }
                scores[nextMarble % scores.size] += nextMarble.toLong() + circle.pollLast().toLong()

                circle.offerLast(circle.pollFirst())
            } else {
                circle.offerLast(circle.pollFirst())
                circle.offerLast(nextMarble)
            }
        }
        return scores.maxOrNull()
    }

    private fun parse(input: String): Pair<Int, Int> {
        val r = "(\\d+) players; last marble is worth (\\d+) points".toRegex()
        val (players, lastMarble) = r.matchEntire(input)!!.destructured
        return players.toInt() to lastMarble.toInt()
    }
}
