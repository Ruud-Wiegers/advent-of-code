package adventofcode.y2023

import adventofcode.io.AdventSolution
import adventofcode.util.collections.combinations
import java.math.BigDecimal

fun main() {
    Day24.solve()
}

object Day24 : AdventSolution(2023, 24, "Never Tell Me The Odds") {

    override fun solvePartOne(input: String): Any {
        return solve(input, 200000000000000.toBigDecimal()..400000000000000.toBigDecimal())

    }

    fun solve(input: String, range: ClosedRange<BigDecimal>): Int {
        val parsed = parse(input)

        val result = parsed.combinations { a, b -> intersection(a, b) }

        return result.filterNotNull().count { (x, y) -> x in range && y in range }
    }

    private fun intersection(a: Hailstone, b: Hailstone): Pair<BigDecimal, BigDecimal>? {
        val v1 = a.position
        val v2 = a.position + a.velocity
        val v3 = b.position
        val v4 = b.position + b.velocity


        val x1 = v1.x
        val y1 = v1.y
        val x2 = v2.x
        val y2 = v2.y
        val x3 = v3.x
        val y3 = v3.y
        val x4 = v4.x
        val y4 = v4.y

        val denominator = ((x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4))
        val numeratorX =
            (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)
        val numeratorY =
            (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)

        return if (denominator.abs() < 0.000000000000001.toBigDecimal()) null
        else {
            val pair = numeratorX / denominator to numeratorY / denominator

            if ((pair.first - x1).signum() != a.velocity.x.signum()) null
            else if ((pair.first - x3).signum() != b.velocity.x.signum()) null
            else pair
        }
    }

    override fun solvePartTwo(input: String): Long {

        //can probably prune this intelligently
        fun generateVelocities() = buildList {
            for (x in -200..200L)
                for (y in -200..200L)
                    add(V3(x.toBigDecimal(), y.toBigDecimal(), BigDecimal.ZERO))
        }

        val stones = parse(input)


        val velocity = generateVelocities().first { throwVelocity ->
            val shifted = stones.map { it.copy(velocity = it.velocity - throwVelocity) }

            val point = intersection(shifted[0], shifted[1]) ?: return@first false

            shifted.all { (hp, hv) ->
                if (hv.x == BigDecimal.ZERO) point.first == hp.x
                else if (hv.y == BigDecimal.ZERO) point.second == hp.y
                else {
                    val tx = (point.first - hp.x) / hv.x
                    val ty = (point.second - hp.y) / hv.y

                    tx == ty

                }
            }
        }

        val shifted1 = stones[0].copy(velocity = stones[0].velocity - velocity)
        val shifted2 = stones[1].copy(velocity = stones[1].velocity - velocity)

        val (x, y) = intersection(shifted1, shifted2)!!

        val t1 = (x - shifted1.position.x) / shifted1.velocity.x
        val z1 = shifted1.position.z + (shifted1.velocity.z * t1)

        val t2 = (x - shifted2.position.x) / shifted2.velocity.x
        val z2 = shifted2.position.z + (shifted2.velocity.z * t2)

        val vz = (z2 - z1) / (t2 - t1)
        val z = z1 - t1 * vz

        return (x + y + z).toLong()

    }
}

private fun parse(input: String) = input.lines().map {
    it.split(", ", " @ ").map { it.trim().toBigDecimal() }.chunked(3) { (x, y, z) -> V3(x, y, z) }
        .let { Hailstone(it[0], it[1]) }
}

private data class Hailstone(val position: V3, val velocity: V3)
private data class V3(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal) {
    operator fun plus(o: V3) = V3(x + o.x, y + o.y, z + o.z)
    operator fun minus(o: V3) = V3(x - o.x, y - o.y, z - o.z)
}