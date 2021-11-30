package adventofcode.y2019

import adventofcode.AdventSolution
import adventofcode.solve
import kotlin.math.absoluteValue
import kotlin.math.sign

fun main() = Day12.solve()

object Day12 : AdventSolution(2019, 12, "The N-body problem") {

    override fun solvePartOne(input: String): Int {
        val moons = parse(input).map { Moon(it, Vec3(0, 0, 0)) }

        repeat(1000) {
            moons.forEach { m ->
                moons.filter { it != m }.forEach { m.attract(it) }
            }
            moons.forEach { it.move() }
        }
        return moons.sumOf { it.energy() }
    }

    override fun solvePartTwo(input: String): Long {
        val positions = parse(input)

        return listOf(Vec3::x, Vec3::y, Vec3::z)
                .map { positions.map(it) }
                .map { 2 * findHalfCycle(it).toLong() }
                .reduce(::lcm)
    }

    private fun findHalfCycle(positions: List<Int>): Int {
        val pv = positions.map { it to 0 }

        fun still(state: List<Pair<Int, Int>>) = state.all { it.second == 0 }

        return evolve(pv).drop(1).takeWhile { !still(it) }.count() + 1
    }

    private fun evolve(state: List<Pair<Int, Int>>) = generateSequence(state) { old ->
        old.map { (p, v) ->
            val vn = v + old.sumOf { (it.first - p).sign }
            p + vn to vn
        }
    }

    private fun lcm(a: Long, b: Long) = a * b / gcd(a, b)

    private tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    private fun parse(input: String): List<Vec3> = input
            .lines()
            .map { """<x=(-?\d+), y=(-?\d+), z=(-?\d+)>""".toRegex().matchEntire(it)!!.destructured }
            .map { (x, y, z) -> Vec3(x.toInt(), y.toInt(), z.toInt()) }


    private data class Vec3(val x: Int, val y: Int, val z: Int) {
        operator fun plus(o: Vec3) = Vec3(x + o.x, y + o.y, z + o.z)
        operator fun minus(o: Vec3) = Vec3(x - o.x, y - o.y, z - o.z)
        fun sign() = Vec3(x.sign, y.sign, z.sign)
        fun energy() = listOf(x, y, z).map { it.absoluteValue }.sum()
    }

    private data class Moon(var p: Vec3, var v: Vec3) {
        fun move() {
            p += v
        }

        fun attract(o: Moon) {
            v += (o.p - p).sign()
        }

        fun energy() = p.energy() * v.energy()
    }
}
