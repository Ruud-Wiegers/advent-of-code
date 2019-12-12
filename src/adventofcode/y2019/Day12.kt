package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import adventofcode.y2017.takeWhileDistinct
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() = Day12.solve()

object Day12 : AdventSolution(2019, 12, "The N-body problem") {

    override fun solvePartOne(input: String): Int {
        val moons = listOf(
                Vec3(17, -12, 13),
                Vec3(x = 2, y = 1, z = 1),
                Vec3(x = -1, y = -17, z = 7),
                Vec3(x = 12, y = -14, z = 18)
        ).map { Moon(it, Vec3(0, 0, 0)) }

        repeat(1000) {
            moons.forEach { m ->
                moons.filter { it != m }.forEach { m.attract(it) }
            }
            moons.forEach { it.move() }
        }
        return moons.sumBy { it.energy() }
    }

    override fun solvePartTwo(input: String):Long {
        val moons = listOf(
                Vec3(17, -12, 13),
                Vec3(x = 2, y = 1, z = 1),
                Vec3(x = -1, y = -17, z = 7),
                Vec3(x = 12, y = -14, z = 18)
        )

        val sx = solveDirection(moons.map { it.x }).toLong()
        val sy = solveDirection(moons.map { it.y }).toLong()
        val sz = solveDirection(moons.map { it.z }).toLong()

        tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

        val a = sx * sy / gcd(sx, sy)
        return a * sz / gcd(a, sz)
    }

    private fun solveDirection(p: List<Int>): Int {

        val state = p to listOf(0, 0, 0, 0)

        val seq = evolve(state)

        return seq.takeWhileDistinct().count()
    }

    private fun evolve(state: Pair<List<Int>, List<Int>>): Sequence<Pair<List<Int>, List<Int>>> {
        return generateSequence(state) { (p, v) ->
            val pn = p.toIntArray()
            val vn = v.toIntArray()
            for (a in 0..3) {
                for (b in 0..3)
                    vn[a] += (p[b] - p[a]).sign
                pn[a] += vn[a]
            }
            pn.toList() to vn.toList()
        }
    }


    data class Vec3(val x: Int, val y: Int, val z: Int) {
        operator fun plus(o: Vec3) = Vec3(x + o.x, y + o.y, z + o.z)
        operator fun minus(o: Vec3) = Vec3(x - o.x, y - o.y, z - o.z)
        fun sign() = Vec3(x.sign, y.sign, z.sign)
        fun energy() = listOf(x, y, z).map { it.absoluteValue }.sum()
    }

    data class Moon(var p: Vec3, var v: Vec3) {
        fun move() {
            p += v
        }

        fun attract(o: Moon) {
            v += (o.p - p).sign()
        }

        fun energy() = p.energy() * v.energy()
    }

}
