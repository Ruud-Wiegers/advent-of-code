package adventofcode.y2016

import adventofcode.AdventSolution

object Day15 : AdventSolution(2016, 15, "Timing is Everything") {
	private val configuration = listOf(
			Disc(17, 15),
			Disc(3, 2),
			Disc(19, 4),
			Disc(13, 2),
			Disc(7, 2),
			Disc(5, 0))

	override fun solvePartOne(input: String) = calulateWaitingTime(configuration).toString()
	override fun solvePartTwo(input: String) = calulateWaitingTime(configuration + Disc(11, 0)).toString()

	private fun calulateWaitingTime(configuration: List<Disc>): Long = configuration
			.mapIndexed { i, d -> d.rotate(i + 1) }
			.reduce(this::combine)
			.let { it.a % it.size }


	private data class Disc(val size: Long, val position: Long) {
		fun rotate(steps: Int): Disc = this.copy(position = (position + steps) % size)

		val a: Long
			get() = size - position
	}

	private fun combine(one: Disc, two: Disc): Disc {
		val gcd = extendedGcd(one.size, two.size)
		val s = one.size * two.size
		val r = one.a * ((two.size * gcd.third) % s) + two.a * ((one.size * gcd.second) % s)
		return Disc(s, (s - r) % s)
	}

	private fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
		var a = a
		var b = b
		val aa = longArrayOf(1, 0)
		val bb = longArrayOf(0, 1)
		while (true) {
			val q = a / b
			a %= b
			aa[0] = aa[0] - q * aa[1]
			bb[0] = bb[0] - q * bb[1]
			if (a == 0L) {
				return Triple(b, aa[1], bb[1])
			}
			val q1 = b / a
			b %= a
			aa[1] = aa[1] - q1 * aa[0]
			bb[1] = bb[1] - q1 * bb[0]
			if (b == 0L) {
				return Triple(a, aa[0], bb[0])
			}
		}
	}


}
/*
Disc #1 has 17 positions; at time=0, it is at position 15.
Disc #2 has 3 positions; at time=0, it is at position 2.
Disc #3 has 19 positions; at time=0, it is at position 4.
Disc #4 has 13 positions; at time=0, it is at position 2.
Disc #5 has 7 positions; at time=0, it is at position 2.
Disc #6 has 5 positions; at time=0, it is at position 0.
 */