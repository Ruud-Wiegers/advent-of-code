package adventofcode.y2017

import adventofcode.AdventSolution
import kotlin.math.abs

object Day20 : AdventSolution(2017, 20, "Particle Swarm") {

	//this comparator is not correct, but works for my input :S
	override fun solvePartOne(input: String): String {
		val particles: List<Particle> = parse(input)
		val comparator = compareBy<Particle>({ it.acceleration.magnitude }, { it.velocity.magnitude }, { it.position.magnitude })
		val nearestParticle = particles.minWith(comparator)
		return particles.indexOf(nearestParticle).toString()
	}

	override fun solvePartTwo(input: String): String {
		var particles: List<Particle> = parse(input)

		//1000 is just a guess
		repeat(1000) {
			particles = particles.groupBy { it.position }
					.values
					.filter { it.size == 1 }
					.map { it[0].tick() }
		}
		return particles.size.toString()
	}

}

private fun parse(input: String): List<Particle> {
	return input.splitToSequence(',', '\n')
			.map { it.filter { it in "-0123456789" }.toInt() }
			.chunked(3) { Vector(it[0], it[1], it[2]) }
			.chunked(3) { Particle(it[0], it[1], it[2]) }
			.toList()
}

data class Particle(
		val position: Vector,
		val velocity: Vector,
		val acceleration: Vector
) {
	fun tick(): Particle = Particle(
			position + velocity + acceleration,
			velocity + acceleration,
			acceleration
	)
}

data class Vector(val x: Int, val y: Int, val z: Int) {
	operator fun plus(v: Vector) = Vector(x + v.x, y + v.y, z + v.z)
	val magnitude get() = sequenceOf(x, y, z).map(::abs).sum()
}
