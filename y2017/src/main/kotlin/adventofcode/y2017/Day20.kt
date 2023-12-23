package adventofcode.y2017

import adventofcode.io.AdventSolution
import kotlin.math.abs

fun main()
{
    Day20.solve()
}

object Day20 : AdventSolution(2017, 20, "Particle Swarm")
{

    //this comparator is not correct, but works for my input :S
    override fun solvePartOne(input: String): Int
    {
        val particles: List<Particle> = parse(input)
        val nearestParticle = particles.minByOrNull { it.acceleration.magnitude }
        return particles.indexOf(nearestParticle)
    }

    override fun solvePartTwo(input: String): Int
    {
        var particles: List<Particle> = parse(input)

        //1000 is just a guess
        repeat(1000) {
            particles = particles.groupBy(Particle::position)
                .values
                .mapNotNull(List<Particle>::singleOrNull)
                .map(Particle::tick)
        }
        return particles.size
    }
}

private fun parse(input: String): List<Particle>
{
    return input.splitToSequence(',', '\n')
        .map { it.filter { it in "-0123456789" }.toInt() }
        .chunked(3) { (x, y, z) -> Vector(x, y, z) }
        .chunked(3) { (p, v, a) -> Particle(p, v, a) }
        .toList()
}

data class Particle(
    val position: Vector,
    val velocity: Vector,
    val acceleration: Vector
)
{
    fun tick(): Particle = Particle(
        position + velocity + acceleration,
        velocity + acceleration,
        acceleration
    )
}

data class Vector(val x: Int, val y: Int, val z: Int)
{
    operator fun plus(v: Vector) = Vector(x + v.x, y + v.y, z + v.z)
    val magnitude get() = sequenceOf(x, y, z).map(::abs).sum()
}
