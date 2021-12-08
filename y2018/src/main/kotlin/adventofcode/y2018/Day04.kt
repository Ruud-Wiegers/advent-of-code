package adventofcode.y2018

import adventofcode.AdventSolution

object Day04 : AdventSolution(2018, 4, "Repose Record") {

    override fun solvePartOne(input: String): Int {
        val schedule = parseSchedule(input)

        val sleepiestGuard = schedule.maxByOrNull { g -> g.value.sumOf { it.last - it.first } }!!
        val sleepiestMinute = (0..59).maxByOrNull { m -> sleepiestGuard.value.count { m in it } }!!

        return sleepiestGuard.key * sleepiestMinute
    }

    override fun solvePartTwo(input: String): Int {
        val schedule = parseSchedule(input)

        val (guard, minute) = schedule.keys
                .flatMap { g -> (0..59).map { m -> g to m } }
                .maxByOrNull { (g, m) -> schedule[g]!!.count { m in it } }!!

        return guard * minute
    }

    private fun parseSchedule(input: String): Map<Int, List<IntRange>> {
        val schedule = mutableMapOf<Int, MutableList<IntRange>>()
        var scheduleOfActiveGuard: MutableList<IntRange>? = null
        var asleep = 0
        input.lineSequence()
                .sorted()
                .map { it.substringAfter(':').substringBefore(']').toInt() to it.substringAfter("] ") }
                .forEach { (minute, message) ->
                    when {
                        '#' in message -> {
                            val guard = message.substringAfter('#').substringBefore(' ').toInt()
                            scheduleOfActiveGuard = schedule.getOrPut(guard) { mutableListOf() }
                        }
                        message == "falls asleep" -> asleep = minute
                        message == "wakes up" -> scheduleOfActiveGuard?.add(asleep until minute)
                    }
                }
        return schedule
    }
}
