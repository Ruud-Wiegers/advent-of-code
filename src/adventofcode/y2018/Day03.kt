package adventofcode.y2018

import adventofcode.AdventSolution

object Day03 : AdventSolution(2018, 3, "No Matter How You Slice It") {

    override fun solvePartOne(input: String): Int {
        val claims = parseInput(input)
        val fabric = applyClaimsToFabric(claims)
        return fabric.count { it > 1 }
    }

    override fun solvePartTwo(input: String): String {
        val claims = parseInput(input)
        val fabric = applyClaimsToFabric(claims)
        return claims.first { claim -> noOverlap(claim, fabric) }.id
    }

    private fun parseInput(input: String): Sequence<Claim> {
        val rule = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)".toRegex()
        return input.splitToSequence("\n")
                .map { rule.matchEntire(it)!!.destructured }
                .map { (id, x, y, w, h) -> Claim(id, x.toInt(), y.toInt(), w.toInt(), h.toInt()) }
    }

    private fun applyClaimsToFabric(claims: Sequence<Claim>) =
            ShortArray(1_000_000).apply {
                claims.forEach { claim ->
                    for (x in claim.x until claim.x + claim.w)
                        for (y in claim.y until claim.y + claim.h)
                            this[1000 * y + x]++
                }
            }

    private fun noOverlap(claim: Claim, fabric: ShortArray): Boolean {
        for (x in claim.x until claim.x + claim.w)
            for (y in claim.y until claim.y + claim.h)
                if (fabric[1000 * y + x] > 1) return false
        return true
    }

    private data class Claim(
            val id: String,
            val x: Int,
            val y: Int,
            val w: Int,
            val h: Int)
}