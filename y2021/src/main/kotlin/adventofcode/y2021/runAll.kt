package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.formattedTime
import adventofcode.solve
import kotlin.system.measureTimeMillis

fun main()
{
    measureTimeMillis {
        listOf(
            Day01
        )
            .forEach(AdventSolution::solve)
    }.let { println(formattedTime(it)) }
}
