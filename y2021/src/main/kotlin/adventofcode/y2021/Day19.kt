package adventofcode.y2021

import adventofcode.io.AdventSolution
import adventofcode.util.collections.cartesian
import adventofcode.util.vector.Vec3

object Day19 : AdventSolution(2021, 19, "Beacon Scanner") {
    override fun solvePartOne(input: String): Any {
        val scanners = parse(input)
        val closed = alignScanners(scanners)
        return closed.flatMap { it.key.beacons }.distinct().size
    }

    override fun solvePartTwo(input: String): Int {
        val scanners = parse(input)
        val closed = alignScanners(scanners)
        return closed.values.cartesian().maxOf { (a, b) -> a.manhattanDistanceTo(b) }
    }

    private fun alignScanners(scanners: List<Scanner>): Map<Scanner, Vec3> {
        val closed = mutableMapOf(scanners[0] to Vec3(0, 0, 0))
        val openScanners = mutableListOf(scanners[0])

        var unmatched = scanners.drop(1)

        while (unmatched.isNotEmpty()) {
            val placed = openScanners.removeLast()

            val matched: Map<Scanner, Scanner?> = unmatched.associateWith { current ->
                current.rotations.find { scanner ->
                    (scanner.fingerprint.keys intersect placed.fingerprint.keys).size >= 66
                }
            }

            unmatched = matched.filterValues { it == null }.keys.toList()

            matched.values.filterNotNull().map { scanner ->
                val matchingDistances = placed.fingerprint.keys intersect scanner.fingerprint.keys
                val (delta, count) = matchingDistances.map { k ->
                    placed.fingerprint.getValue(k) - scanner.fingerprint.getValue(k)
                }
                    .groupingBy { it }
                    .eachCount()
                    .maxByOrNull { it.value }!!
                require(count >= 66)
                val shifted = Scanner(scanner.beacons.map { it + delta })

                openScanners += shifted
                closed += shifted to delta
            }
        }
        return closed
    }
}

private val rotationFns: List<(Vec3) -> Vec3> = listOf(
    { (x, y, z) -> Vec3(x, y, z) },
    { (x, y, z) -> Vec3(x, z, -y) },
    { (x, y, z) -> Vec3(x, -y, -z) },
    { (x, y, z) -> Vec3(x, -z, y) },
    { (x, y, z) -> Vec3(-x, z, y) },
    { (x, y, z) -> Vec3(-x, y, -z) },
    { (x, y, z) -> Vec3(-x, -z, -y) },
    { (x, y, z) -> Vec3(-x, -y, z) },
    { (x, y, z) -> Vec3(y, z, x) },
    { (x, y, z) -> Vec3(y, x, -z) },
    { (x, y, z) -> Vec3(y, -z, -x) },
    { (x, y, z) -> Vec3(y, -x, z) },
    { (x, y, z) -> Vec3(-y, x, z) },
    { (x, y, z) -> Vec3(-y, z, -x) },
    { (x, y, z) -> Vec3(-y, -x, -z) },
    { (x, y, z) -> Vec3(-y, -z, x) },
    { (x, y, z) -> Vec3(z, x, y) },
    { (x, y, z) -> Vec3(z, y, -x) },
    { (x, y, z) -> Vec3(z, -x, -y) },
    { (x, y, z) -> Vec3(z, -y, x) },
    { (x, y, z) -> Vec3(-z, y, x) },
    { (x, y, z) -> Vec3(-z, x, -y) },
    { (x, y, z) -> Vec3(-z, -y, -x) },
    { (x, y, z) -> Vec3(-z, -x, y) }
)


private data class Scanner(val beacons: List<Vec3>) {


    //het verschil tussen alle paren van punten is een soort van fingerprint voor de hele beacon
    //Om een match te zijn van tenminste 12 punten, moeten de verschillen ook matchen. Dus tenminste 12*11/2 matches
    //best wel wat aannames:
    // Geen paren van punten met hetzelfde verschil
    // als er genoeg matches tussen verschillen zijn, dan zal het wel kloppen. (hier zit wel een controle op)
    // de key is om te matchen, de value is om snel de translatie uit te voeren.
    val fingerprint: Map<Vec3, Vec3> by lazy {
        buildMap {
            for (a in beacons.indices) {
                for (b in a + 1..beacons.lastIndex) {
                    put(beacons[a] - beacons[b], beacons[a])
                }
            }
        }
    }

    val rotations: List<Scanner> by lazy {
        rotationFns.map { fn ->
            beacons.map { fn(it) }.sortedWith(compareBy(Vec3::x).thenComparing(Vec3::y).thenComparing(Vec3::z))
                .let(::Scanner)
        }
    }
}

private fun parse(input: String) = input.split("\n\n").map { parseScanner(it) }

private fun parseScanner(input: String) =
    input.lines().drop(1).map(::parseVector)
        .sortedWith(compareBy(Vec3::x).thenComparing(Vec3::y).thenComparing(Vec3::z))
        .let(::Scanner)

private fun parseVector(it: String) = it.split(',').map(String::toInt).let { (x, y, z) -> Vec3(x, y, z) }