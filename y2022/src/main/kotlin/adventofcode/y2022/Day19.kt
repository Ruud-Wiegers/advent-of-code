package adventofcode.y2022

import adventofcode.io.AdventSolution
import kotlin.math.ceil

object Day19 : AdventSolution(2022, 19, "Not Enough Minerals") {

    override fun solvePartOne(input: String) = parse(input)
        .map { bp -> solveWithBlueprint(bp, 24) }
        .sumOf { (i, v) -> i * v }

    override fun solvePartTwo(input: String) = parse(input)
        .take(3)
        .map { bp -> solveWithBlueprint(bp, 32) }
        .map { it.second }
        .reduce(Int::times)

    private fun solveWithBlueprint(bp: Blueprint, time: Int): Pair<Int, Int> {
        val states = List(time + 1) { mutableSetOf<State>() }

        states[0] += State(
            materials = Mats(0, 0, 0, 0),
            delta = Mats(1, 0, 0, 0), 0
        )

        for (i in 0 until time) {
            prune(states[i], time)

            states[i].flatMap { it.next(bp, time) }.groupBy { it.time }.forEach { (index, new) ->
                if (index in states.indices)
                    states[index] += new
            }
            states[i].clear()
        }
        return bp.index to states.last().maxOf { it.materials.geode }
    }


    private fun parse(input: String): Sequence<Blueprint> {
        val regex =
            """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()
        return input.lineSequence()
            .map { regex.matchEntire(it)!!.groupValues.drop(1).map { it.toInt() } }
            .map {
                Blueprint(
                    index = it[0],
                    ore = Mats(it[1], 0, 0, 0),
                    clay = Mats(it[2], 0, 0, 0),
                    obsidian = Mats(it[3], it[4], 0, 0),
                    geode = Mats(it[5], 0, it[6], 0)
                )
            }
    }

    private data class Blueprint(val index: Int, val ore: Mats, val clay: Mats, val obsidian: Mats, val geode: Mats) {
        val maxima = listOf(ore, clay, obsidian, geode).reduce(Mats::max)
    }

    private fun prune(states: MutableSet<State>, finishTime: Int) {
        if (states.isEmpty()) return
        val stepsRemaining = finishTime - states.first().time
        val minimalBestScore = states.maxOf { it.materials.geode + it.delta.geode * stepsRemaining }

        states.retainAll { candidate ->
            val minimalScore = candidate.materials.geode + candidate.delta.geode * stepsRemaining
            val bestDelta = (stepsRemaining * stepsRemaining + 1) / 2
            minimalScore + bestDelta > minimalBestScore
        }
    }


    private data class State(val materials: Mats, val delta: Mats, val time: Int) {
        private fun buildOre(blueprint: Blueprint) =
            State(materials - blueprint.ore, delta.copy(ore = delta.ore + 1), time)

        private fun buildClay(blueprint: Blueprint) =
            State(materials - blueprint.clay, delta.copy(clay = delta.clay + 1), time)

        private fun buildObsidian(blueprint: Blueprint) =
            State(materials - blueprint.obsidian, delta.copy(obsidian = delta.obsidian + 1), time)

        private fun buildGeode(blueprint: Blueprint) =
            State(materials - blueprint.geode, delta.copy(geode = delta.geode + 1), time)

        fun next(blueprint: Blueprint, end: Int): List<State> {
            val candidates = buildList {
                if (delta.obsidian > 0) add(buildGeode(blueprint))
                if (delta.clay > 0 && delta.obsidian < blueprint.maxima.obsidian) add(buildObsidian(blueprint))
                if (delta.clay < blueprint.maxima.clay) add(buildClay(blueprint))
                if (delta.ore < blueprint.maxima.ore) add(buildOre(blueprint))
            }
            val resume = candidates.map {
                val fOre = if (it.materials.ore >= 0) 0.0f else -it.materials.ore.toFloat() / this.delta.ore
                val fClay = if (it.materials.clay >= 0) 0.0f else -it.materials.clay.toFloat() / this.delta.clay
                val fObsidian =
                    if (it.materials.obsidian >= 0) 0.0f else -it.materials.obsidian.toFloat() / this.delta.obsidian

                val steps = ceil(maxOf(fClay, fOre, fObsidian)).toInt() + 1

                it.copy(materials = it.materials + this.delta * steps, time = time + steps)
            }
            val takeNothingAndWait = copy(materials = materials + this.delta * (end - time), time = end)
            return resume + takeNothingAndWait
        }
    }


    private data class Mats(val ore: Int, val clay: Int, val obsidian: Int, val geode: Int) {

        operator fun plus(other: Mats) = Mats(
            ore + other.ore,
            clay + other.clay,
            obsidian + other.obsidian,
            geode + other.geode
        )

        operator fun times(s: Int) = Mats(
            ore * s,
            clay * s,
            obsidian * s,
            geode * s
        )

        operator fun minus(other: Mats) = Mats(
            ore - other.ore,
            clay - other.clay,
            obsidian - other.obsidian,
            geode - other.geode
        )

        fun max(other: Mats) = Mats(
            maxOf(ore, other.ore),
            maxOf(clay, other.clay),
            maxOf(obsidian, other.obsidian),
            maxOf(geode, other.geode)
        )
    }
}