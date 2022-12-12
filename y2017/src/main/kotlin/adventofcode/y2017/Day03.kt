package adventofcode.y2017

import adventofcode.AdventSolution
import adventofcode.util.vector.Vec2
import adventofcode.util.vector.mooreNeighbors
import kotlin.math.roundToInt
import kotlin.math.sqrt

object Day03 : AdventSolution(2017, 3, "Spiral Memory") {
    override fun solvePartOne(input: String) = toSpiralCoordinates(input.toInt() - 1).distance(Vec2.origin)

    override fun solvePartTwo(input: String): Int {
        val spiralCoordinatesSequence = generateSequence(1, Int::inc).map(this::toSpiralCoordinates)

        val spiral = mutableMapOf(Vec2.origin to 1)


        fun Vec2.sumNeighborhood() = mooreNeighbors().mapNotNull(spiral::get).sum()

        return spiralCoordinatesSequence
            .map { spiral.computeIfAbsent(it) { v -> v.sumNeighborhood() } }
            .first { it > input.toInt() }
    }

    private fun toSpiralCoordinates(index: Int): Vec2 {
        if (index <= 0) return Vec2.origin

        //The width of the edge at the current edge of the spiral
        //each half-turn the width increases by one
        val width = sqrt(index.toDouble()).roundToInt()

        //the index of the end of the preceding completed half-turn
        //i.e. the size of the completed inner 'block'
        val spiralBaseIndex = width * (width - 1)

        // 1 or -1, depending on if we've done a complete turn or a half-turn
        val sign = if (width % 2 == 0) 1 else -1

        //distance from origin: the position of spiralBaseIndex is [offset,offset]
        val offset = sign * (width / 2)

        //corner in the middle of the current half-turn
        val cornerIndex = spiralBaseIndex + width

        val x = offset - sign * (index - spiralBaseIndex).coerceAtMost(width)
        val y = offset - sign * (index - cornerIndex).coerceAtLeast(0)

        return Vec2(x, y)
    }
}
