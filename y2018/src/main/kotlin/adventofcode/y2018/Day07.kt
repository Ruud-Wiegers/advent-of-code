package adventofcode.y2018

import adventofcode.io.AdventSolution
import java.util.*

object Day07 : AdventSolution(2018, 7, "The Sum of Its Parts") {

    override fun solvePartOne(input: String): String {
        val prerequisites = parse(input)
        val open = ('A'..'Z').toSortedSet()
        val completed = mutableListOf<Char>()

        while (open.isNotEmpty()) {
            val newTask = open.first { prerequisites[it].orEmpty().none(open::contains) }
            open -= newTask
            completed += newTask
        }

        return completed.joinToString("")
    }

    override fun solvePartTwo(input: String): Int {
        val prerequisites: Map<Char, SortedSet<Char>> = parse(input)
        val open = ('A'..'Z').toSortedSet()
        val tasksInProgress = mutableMapOf<Char, Int>()
        val completed = mutableSetOf<Char>()

        var currentTime = 0

        while (completed.size < 26) {
            //assign new tasks to waiting elves if possible
            open
                .filter { prerequisites[it].orEmpty().all(completed::contains) }
                .take(5 - tasksInProgress.size)
                .forEach { newTaskId ->
                    open -= newTaskId
                    tasksInProgress[newTaskId] = currentTime + 61 + (newTaskId - 'A')
                }

            //advance the time to the completion of the next task
            currentTime = tasksInProgress.values.minOrNull() ?: currentTime

            //move all completed tasks to  'complete'
            tasksInProgress.keys
                .filter { tasksInProgress[it] == currentTime }
                .forEach { completedTaskId ->
                    tasksInProgress -= completedTaskId
                    completed += completedTaskId
                }
        }

        return currentTime
    }

    private fun parse(input: String) = input
        .lineSequence()
        .map {
            val required = it.substringAfter("Step ")[0]
            val next = it.substringAfter("before step ")[0]
            next to required
        }
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.toSortedSet() }
}
