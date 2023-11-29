package adventofcode.y2021

import adventofcode.io.AdventSolution

object Day12 : AdventSolution(2021, 12, "Passage Pathing") {
    override fun solvePartOne(input: String) = solve(input, Path::visit1)

    override fun solvePartTwo(input: String) = solve(input, Path::visit2)

    private inline fun solve(input: String, visit: Path.(cave: String) -> Path?): Int {
        val graph = parseToGraph(input)
        var open = mapOf(Path("start", emptySet(), false) to 1)
        var complete = 0
        while (open.isNotEmpty()) {
            open = open.flatMap { (path, count) ->
                graph[path.current].orEmpty().mapNotNull { path.visit(it) }.map { it to count }
            }
                .groupingBy { it.first }
                .fold(0) { sum, (_, count) -> sum + count }
            complete += open.asSequence().filter { it.key.current == "end" }.sumOf { it.value }
        }
        return complete
    }

    private fun parseToGraph(input: String): Map<String, List<String>> =
        input.lineSequence().map { it.substringBefore('-') to it.substringAfter('-') }
            .flatMap { (a, b) -> listOf(a to b, b to a) }
            .filter { it.second != "start" }
            .filter { it.first != "end" }
            .groupBy({ it.first }, { it.second })

    private data class Path(val current: String, val history: Set<String>, val doubled: Boolean) {
        fun visit1(cave: String) = when {
            cave.lowercase() != cave -> copy(current = cave)
            cave !in history -> copy(current = cave, history = history + cave)
            else -> null
        }

        fun visit2(cave: String) = when {
            cave.lowercase() != cave -> copy(current = cave)
            cave !in history -> copy(current = cave, history = history + cave)
            !doubled -> copy(current = cave, doubled = true)
            else -> null
        }
    }
}
