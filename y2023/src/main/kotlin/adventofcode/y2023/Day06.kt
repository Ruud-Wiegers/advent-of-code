package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.io.solve
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    Day06.solve()
}

object Day06 : AdventSolution(2023, 6, "Wait For It") {

    override fun solvePartOne(input: String): Long {
        val (times, distances) = input.lines().map {
            "\\d+".toRegex().findAll(it).map { it.value.toLong() }
        }

        return times.zip(distances)
            .map { (time, record) -> solveCombinations(time, record) }
            .reduce(Long::times)
    }

    override fun solvePartTwo(input: String): Long {
        val (time, distance) = input.lines().map {
            "\\d+".toRegex().findAll(it).joinToString(separator = "", transform = MatchResult::value).toLong()
        }

        return solveCombinations(time, distance)
    }
}


private fun solveCombinations(time: Long, distanceToBeat: Long): Long {

    // Quadratic equation: tb - b² > d
    // t: total race time, b: button press milli's, d: distance to beat
    // So we use the quadratic formula to find the intercepts
    val d = sqrt(time * time - 4.0 * distanceToBeat)

    val t1 = (time - d) / 2.0
    val t2 = (time + d) / 2.0

    // A little bit of shenanigans to work around the case where x1 or x2 is an integer
    // We have to strictly beat the record.
    val firstFaster = floor(t1 + 1).toLong()
    val lastFaster = ceil(t2 - 1).toLong()

    return lastFaster - firstFaster + 1
}

