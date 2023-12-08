package adventofcode.util.math


fun lcm(a: Long, b: Long) = a / extendedGcd(a, b).first * b

fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
    var a1 = a
    var b1 = b
    val aa = longArrayOf(1, 0)
    val bb = longArrayOf(0, 1)
    while (true) {
        val q = a1 / b1
        a1 %= b1
        aa[0] -= q * aa[1]
        bb[0] -= q * bb[1]
        if (a1 == 0L) {
            return Triple(b1, aa[1], bb[1])
        }
        val q1 = b1 / a1
        b1 %= a1
        aa[1] -= q1 * aa[0]
        bb[1] -= q1 * bb[0]
        if (b1 == 0L) {
            return Triple(a1, aa[0], bb[0])
        }
    }
}
