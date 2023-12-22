package adventofcode.util.vector

import kotlin.math.abs
import kotlin.math.sign

data class Vec3(val x: Int, val y: Int, val z: Int) {
    operator fun plus(o: Vec3) = Vec3(x + o.x, y + o.y, z + o.z)
    operator fun minus(o: Vec3) = Vec3(x - o.x, y - o.y, z - o.z)

    fun manhattanDistanceTo(o: Vec3) = (this - o).let { abs(it.x) + abs(it.y) + abs(it.z) }

    companion object {
        val origin = Vec3(0, 0, 0)
    }

    val sign get() = Vec3(x.sign,y.sign,z.sign)
}