package adventofcode.y2022

import adventofcode.io.AdventSolution


object Day16 : AdventSolution(2022, 16, "Proboscidea Volcanium") {

    override fun solvePartOne(input: String): Int {
        val valves = parse(input)
        val interestingValves = valves.filter { it.rate > 0 || it.name == "AA" }.associate { it.name to it.rate }
        val distances = distances(interestingValves.keys, valves.associate { it.name to it.neighbors })


        return solvePartial(interestingValves, distances, 30)

    }


    override fun solvePartTwo(input: String): Int {
        val valves = parse(input)
        val interestingValves = valves.filter { it.rate > 0 || it.name == "AA" }.associate { it.name to it.rate }

        val distances = distances(interestingValves.keys, valves.associate { it.name to it.neighbors })

        val excluded = setOf(interestingValves.keys.first { it != "AA" }, "AA")

        val ps = interestingValves.keys.filter { it !in excluded }.powerset()

        return ps.maxOf { toInclude ->
            val humanSet = interestingValves.filterKeys { it in toInclude + "AA" }
            val elephantSet = interestingValves.filterKeys { it !in toInclude }


            solvePartial(humanSet, distances, 26) + solvePartial(elephantSet, distances, 26)
        }
    }

    private fun solvePartial(
        interestingValves: Map<String, Int>,
        distances: Map<String, Map<String, Int>>,
        bound: Int
    ): Int {
        val partials = List(bound) { mutableSetOf<Partial>() }
        partials[0] += Partial(setOf("AA"), "AA", 0)
        for (t in partials.indices) {
            val scoreToBeat = partials.drop(t).maxOfOrNull { it.maxOfOrNull { it.score } ?: 0 } ?: 0

            for (old in partials[t]) {

                //prune if we can never catch up
                //heuristic: we open the best valve every 2 minutes (minimum distance is 2)
                val maximalRate = interestingValves
                    .filterKeys { it !in old.opened }
                    .values
                    .sortedDescending()
                    .scan(0, Int::plus)
                    .take((bound - t) / 2)
                    .sum()
                if (old.score + maximalRate <= scoreToBeat) continue

                for ((goal, rate) in interestingValves) {
                    if (goal in old.opened) continue
                    val time = distances.getValue(old.position).getValue(goal) + 1
                    if (t + time >= bound) continue

                    partials[t + time] += Partial(
                        opened = old.opened + goal,
                        position = goal,
                        score = old.score + (bound - (t + time)) * rate
                    )
                }
            }
        }

        return partials.flatten().maxOf { it.score }
    }
}


data class Partial(val opened: Set<String>, val position: String, val score: Int)

private fun parse(input: String): List<Valve> = input.lines().map { line ->
    Valve(
        name = line.substringAfter("Valve ").substringBefore(" "),
        rate = line.substringAfter("rate=").substringBefore(";").toInt(),
        neighbors = line.substringAfter(" to valve").substringAfter(" ").split(", ")
    )
}

private fun distances(
    interestingValves: Set<String>,
    links: Map<String, List<String>>
): Map<String, Map<String, Int>> {


    fun bfs(start: String, reachableNeighbors: (String) -> Iterable<String>): List<Set<String>> =
        generateSequence(Pair(setOf(start), setOf(start))) { (frontier, visited) ->
            val unexploredNeighbors = frontier.flatMap(reachableNeighbors).toSet() - visited
            Pair(unexploredNeighbors, visited + unexploredNeighbors)
        }
            .takeWhile { (frontier, _) -> frontier.isNotEmpty() }
            .map { it.first }
            .toList()

    return interestingValves.associateWith { start ->
        bfs(start) { str -> links[str].orEmpty() }.map { it.filter { it in interestingValves } }
            .flatMapIndexed { index, ends ->
                ends.filter { it != start && it != "AA" }.map { it to index }

            }.toMap()
    }
}

private data class Valve(val name: String, val rate: Int, val neighbors: List<String>)


private fun List<String>.powerset(): List<List<String>> {
    return if (this.isEmpty()) listOf(listOf())
    else listOf(listOf(), take(1)).flatMap { strings: List<String> ->
        drop(1).powerset().map { it + strings }

    }
}