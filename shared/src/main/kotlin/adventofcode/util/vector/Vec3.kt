package adventofcode.util.vector

data class Vec3(val x: Int, val y: Int, val z: Int)
{
    operator fun plus(o: Vec3) = Vec3(x + o.x, y + o.y, z + o.z)

    companion object{
        val origin = Vec3(0,0,0)
    }
}