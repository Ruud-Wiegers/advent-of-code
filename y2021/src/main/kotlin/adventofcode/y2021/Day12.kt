package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main() {
    Day12.solve()
}

object Day12 : AdventSolution(2021, 12, "Passage Pathing") {
    override fun solvePartOne(input: String) = solve(input, Path::visit1)

    override fun solvePartTwo(input: String) = solve(input, Path::visit2)

    private inline fun solve(input: String, visit: Path.(cave: Cave) -> Path?): Int {
        val graph = parseInput(input)
        var open = listOf(Path(Cave.Start, emptySet(), false))
        var complete = 0
        while (open.isNotEmpty()) {
            open = open.flatMap { path -> graph[path.current].orEmpty().mapNotNull { path.visit(it) } }
            complete += open.count { it.current == Cave.End }
        }
        return complete
    }

    private data class Path(val current: Cave, val history: Set<Cave.Small>, val doubled: Boolean) {
        fun visit1(cave: Cave) = when (cave) {
            !is Cave.Small -> copy(current = cave)
            !in history -> copy(current = cave, history = history + cave)
            else -> null
        }

        fun visit2(cave: Cave) = when {
            cave !is Cave.Small -> copy(current = cave)
            cave !in history -> copy(current = cave, history = history + cave)
            !doubled -> copy(current = cave, doubled = true)
            else -> null
        }
    }

    private fun parseInput(input: String) =
        input.lineSequence().map { it.substringBefore('-').toCave() to it.substringAfter('-').toCave() }
            .flatMap { (a, b) -> listOf(a to b, b to a) }
            .filter { it.second != Cave.Start }
            .filter { it.first != Cave.End }
            .groupBy({ it.first }, { it.second })

    private fun String.toCave() = when (this) {
        "start" -> Cave.Start
        "end" -> Cave.End
        lowercase() -> Cave.Small(this)
        else -> Cave.Large(this)
    }

    private sealed class Cave {
        object Start : Cave()
        object End : Cave()
        data class Large(val text: String) : Cave()
        data class Small(val text: String) : Cave()
    }
}
