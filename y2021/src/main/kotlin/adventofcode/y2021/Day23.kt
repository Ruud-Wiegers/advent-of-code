package adventofcode.y2021

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import java.util.PriorityQueue

fun main() {
    Day23.solve()
}

object Day23 : AdventSolution(2021, 23, "Amphipods") {
    override fun solvePartOne(input: String) = solve(
        AmphipodState(
            corridor = "       ",
            rooms = listOf("DB", "AA", "BD", "CC"),
            finishedCounts = listOf(2, 2, 2, 2)
        )
    )

    override fun solvePartTwo(input: String) = solve(
        AmphipodState(
            corridor = "       ",
            rooms = listOf("DDDB", "ACBA", "BBAD", "CACC"),
            finishedCounts = listOf(4, 4, 4, 4)
        )
    )

    private fun solve(initial: AmphipodState): Int? {
        val closed = mutableMapOf(initial to 0)

        val open = PriorityQueue<AmphipodState>(compareBy { closed.getValue(it) })
        open += initial

        while (open.isNotEmpty()) {
            val candidate = open.poll()
            if (candidate.isGoal()) return closed.getValue(candidate)

            candidate.tryMoves()
                .filter { (newState, cost) -> (closed[newState] ?: Int.MAX_VALUE) > closed.getValue(candidate) + cost }
                .forEach { (newState, cost) ->
                    closed[newState] = closed.getValue(candidate) + cost
                    open.add(newState)
                }
        }

        return null
    }
}

private data class AmphipodState(val corridor: String, val rooms: List<String>, val finishedCounts: List<Int>) {

    fun isGoal() = finishedCounts.sum() == 0

    fun tryMoves(): List<Pair<AmphipodState, Int>> = listOf(
        stepIntoCorridor(0),
        stepIntoCorridor(1),
        stepIntoCorridor(2),
        stepIntoCorridor(3),
        stepIntoDestination(0, 'A'),
        stepIntoDestination(1, 'B'),
        stepIntoDestination(2, 'C'),
        stepIntoDestination(3, 'D')
    ).flatten()

    fun stepIntoCorridor(iFromRoom: Int): List<Pair<AmphipodState, Int>> {
        if (rooms[iFromRoom].isEmpty()) return emptyList()

        val targets = (iFromRoom + 1 downTo 0).takeWhile { corridor[it] == ' ' } + ((iFromRoom + 2)..6).takeWhile { corridor[it] == ' ' }

        return targets.map { iPos ->
            val symbol = rooms[iFromRoom][0]
            val newState = copy(
                corridor = corridor.replaceRange(iPos..iPos, symbol.toString()),
                rooms = rooms.toMutableList().apply { this[iFromRoom] = rooms[iFromRoom].drop(1) }
            )
            val cost = rooms[iFromRoom].sumOf(::cost) + cost(symbol) * distances[iFromRoom][iPos]
            newState to cost
        }
    }

    fun stepIntoDestination(iToRoom: Int, symbol: Char): List<Pair<AmphipodState, Int>> {
        if (rooms[iToRoom].isNotEmpty()) return emptyList()

        val sources = listOfNotNull(
            (iToRoom + 1 downTo 0).firstOrNull { corridor[it] != ' ' },
            ((iToRoom + 2)..6).firstOrNull { corridor[it] != ' ' }).filter { corridor[it] == symbol }

        return sources.map { iPos ->
            val newState = copy(
                corridor = corridor.replaceRange(iPos..iPos, " "),
                finishedCounts = finishedCounts.toMutableList().apply { this[iToRoom]-- }
            )
            val cost = cost(symbol) * (finishedCounts[iToRoom] + distances[iToRoom][iPos])
            newState to cost
        }
    }
}

private val distances = listOf(
    listOf(2, 1, 1, 3, 5, 7, 8),
    listOf(4, 3, 1, 1, 3, 5, 6),
    listOf(6, 5, 3, 1, 1, 3, 4),
    listOf(8, 7, 5, 3, 1, 1, 2)
)

private fun cost(char: Char) = when (char) {
    'A' -> 1
    'B' -> 10
    'C' -> 100
    'D' -> 1000
    else -> 0
}

