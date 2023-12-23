package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.Vec3

fun main() {
    Day22.solve()
}

object Day22 : AdventSolution(2023, 22, "Sand Slabs") {

    override fun solvePartOne(input: String): Any {
        val slabsInAir = parse(input)

        val columns: Map<Vec2, List<Int>> = buildColumns(slabsInAir)

        val nextAbove = nextSlabsInDirection(slabsInAir, columns, 1)
        val nextBelow = nextSlabsInDirection(slabsInAir, columns, -1)

        val slabs = turnOnGravity(slabsInAir, nextBelow)

        val touchingBelow = nextBelow.mapIndexed { iHigh, lower ->
            lower.filter { iLow -> slabs[iHigh].lowest() - 1 == slabs[iLow].highest() }
        }

        val touchingAbove = nextAbove.mapIndexed { iLow, higher ->
            higher.filter { iHigh -> slabs[iHigh].lowest() - 1 == slabs[iLow].highest() }
        }



        return slabs.indices.count { removedIndex ->
            touchingAbove[removedIndex].all { touchingBelow[it].size > 1 }
        }
    }


    override fun solvePartTwo(input: String): Any {
        val slabsInAir = parse(input)

        val columns: Map<Vec2, List<Int>> = buildColumns(slabsInAir)

        val nextAbove = nextSlabsInDirection(slabsInAir, columns, 1)
        val nextBelow = nextSlabsInDirection(slabsInAir, columns, -1)

        val slabs = turnOnGravity(slabsInAir, nextBelow)

        val touchingBelow = nextBelow.mapIndexed { iHigh, lower ->
            lower.filter { iLow -> slabs[iHigh].lowest() - 1 == slabs[iLow].highest() }
        }
        val touchingAbove = nextAbove.mapIndexed { iLow, higher ->
            higher.filter { iHigh -> slabs[iHigh].lowest() - 1 == slabs[iLow].highest() }
        }


        fun disintegrate(index: Int): Int {
            val removed = mutableSetOf(index)
            val incomplete = mutableListOf(index)

            while (incomplete.isNotEmpty()) {
                val iRemoved = incomplete.removeLast()

                val falling = touchingAbove[iRemoved]
                    .filter { it !in removed }
                    .filter { touchingBelow[it].all { sup-> sup in removed } }

                incomplete += falling
                removed += falling
            }

            return removed.size - 1
        }


        return slabs.indices.sumOf { disintegrate(it) }
    }

    private fun turnOnGravity(slabsInAir: List<Slab>, nextBelow: List<Set<Int>>): List<Slab> {
        val slabs = slabsInAir.toMutableList()
        val unmoved = slabs.indices.toMutableSet()
        while (unmoved.isNotEmpty()) {

            val canFall = unmoved.filter { nextBelow[it].none { it in unmoved } }

            unmoved -= canFall

            for (falling in canFall) {

                val heightOfSlabsBelow = nextBelow[falling].maxOfOrNull { slabs[it].highest() } ?: 0
                val currentHeight = slabs[falling].lowest()
                val delta = Vec3(0, 0, currentHeight - heightOfSlabsBelow - 1)
                slabs[falling] = slabs[falling].let { Slab(it.start - delta, it.end - delta) }
            }
        }
        return slabs
    }

    private fun nextSlabsInDirection(slabs: List<Slab>, columns: Map<Vec2, List<Int>>, direction: Int) =
        slabs.mapIndexed { index, slab ->
            slab.cubes.map { c -> Vec2(c.x, c.y) }
                .mapNotNull {
                    val stack = columns.getValue(it)
                    val above = stack.indexOf(index) + direction
                    stack.getOrNull(above)
                }.toSet()
        }

    private fun buildColumns(slabsInAir: List<Slab>): Map<Vec2, List<Int>> = slabsInAir
        .flatMapIndexed { i, s -> s.cubes.map { c -> Vec2(c.x, c.y) to i } }
        .groupBy({ it.first }, { it.second })
        .mapValues { (_, v) -> v.distinct().sortedBy { slabsInAir[it].lowest() } }

}


private fun parse(input: String) = input.lines()
    .map {
        it.split("~", ",")
            .map { it.toInt() }.chunked(3) { (x, y, z) -> Vec3(x, y, z) }
    }
    .map { Slab(it[0], it[1]) }


private data class Slab(val start: Vec3, val end: Vec3) {

    val cubes =
        (end - start).sign.let { step -> generateSequence(start, step::plus).takeWhile { it != end } + end }.toSet()

    fun lowest() = minOf(start.z, end.z)
    fun highest() = maxOf(start.z, end.z)

}
