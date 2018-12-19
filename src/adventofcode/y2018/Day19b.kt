package adventofcode.y2018


fun main(){
    println( solve())

}

private fun solve(): Int {
    return fastDivisorSum(initialize(false))
}

private fun initialize(a:Boolean): Int {
    var r0 = if (a) 0 else 1
    var r1 = 0
    var r2 = 0
    var r4 = 0
    var r5 = 0

    r2 += 2
    r2 *= r2
    r2 *= 19
    r2 *= 11
    r4 += 2
    r4 *= 22
    r4 += 2
    r2 += r4
    if (r0 == 1) {
        r4 = 27
        r4 *= 28
        r4 += 29
        r4 *= 30
        r4 *= 14
        r4 *= 32
        r2 += r4
    }
    r0 = 0
    r1 = 1

    println("$r0 $r1 $r2 $r4 $r5 ")
    return r2
}

//sum of divisors!
private fun magic(r2: Int): Int {
    var r0 = 0
    repeat(r2) { y ->
        repeat( r2) { x ->
            if ((y+1) * (x+1) == r2) r0 += (y+1)
        }
    }
    return r0
}

private fun fastDivisorSum(n: Int) = (1..n).asSequence().filter { n % it == 0 }.sum()