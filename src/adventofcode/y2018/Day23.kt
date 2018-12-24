package adventofcode.y2018

import adventofcode.AdventSolution
import adventofcode.solve
import java.util.*
import kotlin.math.abs


//This one is a mess. Tried lots of things, still experimenting with alternatives
//got lucky
fun main(args: Array<String>) {
    Day23.testNeighbors()
    Day23.solve()
}

object Day23 : AdventSolution(2018, 23, "Experimental Emergency Teleportation") {

    override fun solvePartOne(input: String): Int {
        val bots = parse(input)

        val best = bots.maxBy(NanoBot::r)!!

        return bots.count { it.p in best }
    }

    override fun solvePartTwo(input: String): Long {
        val bots = parse(input)

        val cluster = bots.toMutableSet()

        //guessing there's only one clique
        cluster.removeIf { bots.count { b -> it.overlaps(b) } < 500 }
        require(cluster.all { c -> cluster.all(c::overlaps) })

        val smallest = cluster.minBy { it.r }!!

        val start = Random(0)
                .let { r -> generateSequence { r.nextInt(smallest.r.toInt() * 2) }.map { it - smallest.r } }
                .asSequence()
                .chunked(3)
                .map { (x, y, z) -> Point3D(x, y, z) }
                .map { smallest.p + it }
                .first {
                    val x = cluster.count { b -> it in b }
                    x == 970
                }

        println(start.distance(Point3D(0, 0, 0)))
        println(descend(cluster.toList(), start).distance(Point3D(0, 0, 0)))


        var currentRadius = 100_000_000L
        val origin = Point3D(0, 0, 0)
        var currentBots = setOf(NanoBot(origin, currentRadius))

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
            val maxDistance = newGeneration.map { it.second }.max() ?: 0

            currentBots = newGeneration.filter { it.second == maxDistance }.map { it.first }.toSet()
        }


        return currentBots.map { origin.distance(it.p) }.first()
    }

    private fun descend(cluster: List<NanoBot>, p: Point3D): Point3D {
        var hi = p
        var lo = Point3D(0, 0, 0)
        while (hi.distance(lo) > 1) {
            val mid = (hi + lo).halve()
            if (cluster.all { mid in it }) {
                hi = mid
            } else {
                lo = mid
            }
        }
        return hi
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
        fun halve() = Point3D(x / 2, y / 2, z / 2)

    }

    private data class NanoBot(val p: Point3D, val r: Long) {
        fun overlaps(other: NanoBot) = p.distance(other.p) <= r + other.r
        operator fun contains(other: Point3D) = p.distance(other) <= r
    }

    private fun Point3D.neighbors(d: Long): Iterable<Point3D> =
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

    fun testNeighbors() {

        val p = Point3D(0, 0, 0)
        val b = NanoBot(p, 8)
        val c = p.neighbors(4).map { NanoBot(it, 4) }.count { it.overlaps(b) }
        println(c)
    }
}