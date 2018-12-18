package adventofcode.y2018

import adventofcode.AdventSolution


object Day17 : AdventSolution(2018, 17, "Reservoir Research") {

    override fun solvePartOne(input: String): Int {
        val blocked = parse(input)
        val flowingWater = mutableSetOf(Point(500, 0))
        val stillWater = mutableSetOf<Point>()

        val yRange = blocked.minBy { it.y }!!.y..blocked.maxBy { it.y }!!.y

        do {
            var changes = false
            for (w in flowingWater.toList()) {
                val down = w.copy(y = w.y + 1)
                if (down.y > yRange.last) continue
                if (down !in blocked) {
                    val add = flowingWater.add(down)
                    changes = changes || add
                } else {
                    val left = w.copy(x = w.x - 1)
                    if (left !in blocked) {
                        val add = flowingWater.add(left)
                        changes = changes || add
                    } else {
                        val xs = generateSequence(w.x) { it + 1 }.takeWhile { w.copy(x = it) in flowingWater }.toList()
                        if (xs.isNotEmpty() && w.copy(x = xs.last() + 1) in blocked && xs.all { Point(it, w.y + 1) in blocked }) {
                            xs.map { w.copy(x = it) }
                                    .forEach {
                                        blocked.add(it)
                                        stillWater.add(it)
                                        flowingWater.remove(it)
                                    }
                            changes = true
                        }
                    }

                    val right = w.copy(x = w.x + 1)
                    if (right !in blocked) {
                        val add = flowingWater.add(right)
                        changes = changes || add
                    } else {
                        val xs = generateSequence(w.x) { it - 1 }.takeWhile { w.copy(x = it) in flowingWater }.toList()
                        if (xs.isNotEmpty() && w.copy(x = xs.last() + 1) in blocked && xs.all { Point(it, w.y + 1) in blocked }) {
                            xs.map { w.copy(x = it) }
                                    .forEach {
                                        blocked.add(it)
                                        stillWater.add(it)
                                        flowingWater.remove(it)
                                    }
                            changes = true
                        }
                    }
                }
            }

        } while (changes)
        println((stillWater).count { it.y in yRange })

        return (flowingWater + stillWater).count { it.y in yRange }

    }

    override fun solvePartTwo(input: String) = ""

    private fun parse(input: String): MutableSet<Point> {
        val clay = mutableSetOf<Point>()
        val regex = "(.).*?(\\d+).*?(\\d+).*?(\\d+).*?".toRegex()
        input.splitToSequence("\n")
                .map { regex.matchEntire(it)!!.destructured }
                .forEach { (o, a, b1, b2) ->
                    if (o == "x")
                        for (y in b1.toInt()..b2.toInt()) clay += Point(a.toInt(), y)
                    else
                        for (x in b1.toInt()..b2.toInt()) clay += Point(x, a.toInt())
                }
        return clay
    }

    data class Point(val x: Int, val y: Int)
}
