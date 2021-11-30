package adventofcode.y2018

import adventofcode.AdventSolution
import kotlin.math.abs

object Day23 : AdventSolution(2018, 23, "Experimental Emergency Teleportation") {

    override fun solvePartOne(input: String): Int {
        val bots = parse(input)

        val best = bots.maxByOrNull(NanoBot::r)!!

        return bots.count { it.p in best }
    }

    override fun solvePartTwo(input: String): Long? {
        val bots = parse(input)

        //guessing there's one clique
        val cluster = bots.filter { bots.count { b -> it.overlaps(b) } > 500 }.toSet()
        require(cluster.all { c -> cluster.all(c::overlaps) })

        //guessing there's at least one point in the remaining swarm that's covered by all bots.
        //also guessing this area is small.

        return cluster
            .map(::PlanarRepresentation)
            .reduce(PlanarRepresentation::intersection)
            .boundingCube()
            .filter { p -> cluster.all { p in it } }
            .map { abs(it.x) + abs(it.y) + abs(it.z) }
            .minOrNull()
    }

    private fun parse(input: String): List<NanoBot> = input
            .filter { it in "-0123456789,\n" }
            .splitToSequence(",", "\n")
            .map(String::toLong)
            .chunked(4) { (x, y, z, r) -> NanoBot(Point3D(x, y, z), r) }
            .toList()

    private data class Point3D(val x: Long, val y: Long, val z: Long) {
        fun distance(other: Point3D) = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
        infix operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)
    }

    private data class NanoBot(val p: Point3D, val r: Long) {
        fun overlaps(other: NanoBot) = p.distance(other.p) <= r + other.r
        operator fun contains(other: Point3D) = p.distance(other) <= r
    }


    /* planar representation of axis-oriented octahedra:
                   z-       z+
      y+
                 L3|L0     H1|H2
      0   x+     --+--     --+--
                 L2|L1     H0|H3

      The planes are represented by the z-value of the point on the plane where x=0 and y=0
           */
    private data class PlanarRepresentation(val low: List<Long>, val high: List<Long>) {
        constructor(b: NanoBot) : this(
                listOf(
                        -b.p.x - b.p.y + b.p.z - b.r,
                        -b.p.x + b.p.y + b.p.z - b.r,
                        b.p.x + b.p.y + b.p.z - b.r,
                        b.p.x - b.p.y + b.p.z - b.r),
                listOf(
                        -b.p.x - b.p.y + b.p.z + b.r,
                        -b.p.x + b.p.y + b.p.z + b.r,
                        b.p.x + b.p.y + b.p.z + b.r,
                        b.p.x - b.p.y + b.p.z + b.r))

        init {
            check(isValid())
        }

        fun intersection(o: PlanarRepresentation) = PlanarRepresentation(
                low.zip(o.low, ::maxOf), high.zip(o.high, ::minOf)
        )

        fun isValid() = low.zip(high).all { (l, h) -> l <= h }

        fun x() = (low[2] + low[3] - high[0] - high[2]) / 2..(high[2] + high[3] - low[0] - low[2]) / 2
        fun y() = (low[1] + low[2] - high[0] - high[2]) / 2..(high[1] + high[2] - low[0] - low[2]) / 2
        fun z() = (low[0] + low[2]) / 2..(high[0] + high[2]) / 2

        fun boundingCube() = sequence {
            for (x in x())
                for (y in y())
                    for (z in z())
                        yield(Point3D(x, y, z))
        }

    }


    private fun partitioningSolve(cluster: MutableSet<Day23.NanoBot>): Day23.NanoBot {

        fun Day23.Point3D.neighbors(d: Long): Iterable<Day23.Point3D> =
                (-1L..1L).flatMap { xd ->
                    (-1L..1L).flatMap { yd ->
                        (-1L..1L).map { zd ->
                            copy(
                                    x = x + xd * d,
                                    y = y + yd * d,
                                    z = z + zd * d
                            )
                        }
                    }
                }

        var currentRadius = 100_000_000L
        val origin = Day23.Point3D(0, 0, 0)
        var currentBots = setOf(Day23.NanoBot(origin, currentRadius))

        while (currentRadius > 0) {
            currentRadius = (currentRadius / 2) + if (currentRadius > 2) 1 else 0

            val newGeneration = currentBots.flatMap { bot ->
                bot.p.neighbors(currentRadius).map { c ->
                    bot.copy(p = c, r = currentRadius).let { newBot ->
                        newBot to cluster.count {
                            newBot.overlaps(it)
                        }
                    }
                }
            }
            val maxBots = newGeneration.map { it.second }.maxOrNull() ?: 0

            currentBots = newGeneration.filter { it.second == maxBots }.map { it.first }.toSet()
        }

        return currentBots.minByOrNull { it: NanoBot -> origin.distance(it.p) }!!
    }
}
