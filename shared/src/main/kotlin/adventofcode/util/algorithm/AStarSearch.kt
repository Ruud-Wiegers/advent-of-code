package adventofcode.util.algorithm

import java.util.*

interface IState {

    fun getNeighbors(): Sequence<IState>

    val isGoal: Boolean

    val heuristic: Int

}

fun aStar(start: IState): PathfinderState? {
    val openList = PriorityQueue(compareBy<PathfinderState> { it.expectedCost + it.cost })
    openList.add(PathfinderState(start, 0, start.heuristic))
    val closedList = mutableSetOf(start)

    var i = 0
    while (openList.isNotEmpty()) {
        i++
        val candidate = openList.poll()
        if (candidate.IState.isGoal) {
            return candidate
        } else {
            candidate.move()
                .filterNot { it.IState in closedList }
                .forEach {
                    openList.add(it)
                    closedList.add(it.IState)
                }

        }
    }
    return null
}

fun aStarExhaustive(start: IState): Sequence<PathfinderState> = sequence {
    val openList = PriorityQueue(compareBy<PathfinderState> { it.expectedCost + it.cost })
    openList.add(PathfinderState(start, 0, start.heuristic))
    val closedList = mutableSetOf(start)

    var i = 0
    while (openList.isNotEmpty()) {
        i++
        val candidate = openList.poll()
        if (candidate.IState.isGoal) {
            yield(candidate)
        } else {
            candidate.move()
                .filterNot { it.IState in closedList }
                .forEach {
                    openList.add(it)
                    closedList.add(it.IState)
                }

        }
    }
}

data class PathfinderState(
    val IState: IState,
    val cost: Int,
    val expectedCost: Int

) {
    fun move(): Sequence<PathfinderState> = IState.getNeighbors()
        .map {
            copy(IState = it, cost = cost + 1, expectedCost = cost + 1 + it.heuristic)
        }
}