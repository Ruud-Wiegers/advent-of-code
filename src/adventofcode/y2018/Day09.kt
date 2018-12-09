package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import java.util.*

fun main(args: Array<String>) = Day09.solve()

object Day09 : AdventSolution(2018, 9, "Marble Mania") {

    override fun solvePartOne(input: String): Long? {
        val (players, highestMarble) = parse(input)
        return linkedMarbleGame(players, highestMarble)
    }

    override fun solvePartTwo(input: String): Long? {
        val (players, highestMarble) = parse(input)

        return linkedMarbleGame(players, highestMarble * 100)
    }

    private fun linkedMarbleGame(players: Int, highestMarble: Int): Long? {
        val scores = LongArray(players)
        var currentElf = 0


        val circle = LinkedList<Int>()
        circle.add(0)
        var currentPosition = circle.listIterator()


        for (nextMarble in 1..highestMarble) {
            if (nextMarble % 23 != 0) {
                    if (currentPosition.hasNext()) currentPosition.next()
                    else{ currentPosition = circle.listIterator()
                        currentPosition.next()
                }
                currentPosition.add(nextMarble)
                currentElf = (currentElf + 1) % scores.size
            } else {
                scores[currentElf] += nextMarble.toLong()
                repeat(7) {
                    if (currentPosition.hasPrevious()) currentPosition.previous()
                    else{
                        currentPosition = circle.listIterator(circle.size)
                        currentPosition.previous()

                    }
                }
                val m = if (currentPosition.hasPrevious()) currentPosition.previous() else {
                    currentPosition = circle.listIterator(circle.size)
                    currentPosition.previous()
                }

                currentPosition.remove()
                currentPosition.next()

                scores[currentElf] += m.toLong()

                currentElf = (currentElf + 1) % scores.size
            }
        }
        return scores.max()
    }

    private fun parse(input: String): Pair<Int, Int> {
        val players = input.substringBefore(" ").toInt()
        val marbles = input.substringAfter("worth ").substringBefore(" ").toInt()
        return players to marbles
    }

}
