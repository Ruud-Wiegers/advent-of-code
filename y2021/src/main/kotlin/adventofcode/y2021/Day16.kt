package adventofcode.y2021

import adventofcode.io.AdventSolution

object Day16 : AdventSolution(2021, 16, "Packet Decoder") {
    override fun solvePartOne(input: String) = PacketDecoder(input).parsePacket().let(::sumOfVersions)
    override fun solvePartTwo(input: String) = PacketDecoder(input).parsePacket().let(::eval)
}

private sealed class Packet(val version: Int, val type: Int)
private class Literal(version: Int, type: Int, val data: Long) : Packet(version, type)
private class Operator(version: Int, type: Int, val data: List<Packet>) : Packet(version, type)

private class PacketDecoder(input: String) {
    private val binary = "F$input".toBigInteger(16).toString(2).drop(4)
    private var index = 0
    private fun readBits(n: Int): Int = binary.substring(index, index + n).toInt(2).also { index += n }

    fun parsePacket(): Packet {
        val version = readBits(3)
        val type = readBits(3)
        return if (type == 4) Literal(version, type, parseLiteral())
        else Operator(version, type, parseSubpackets())
    }

    private fun parseLiteral(): Long {
        var result = 0L
        do {
            val next = readBits(1) == 1
            result *= 16
            result += readBits(4)
        } while (next)
        return result
    }

    private fun parseSubpackets(): List<Packet> {
        return if (readBits(1) == 0) {
            val lastBit = readBits(15) + index
            buildList {
                while (index < lastBit) {
                    add(parsePacket())
                }
            }
        } else {
            val packetCount = readBits(11)
            List(packetCount) { parsePacket() }
        }
    }
}

private fun sumOfVersions(p: Packet): Int = when (p) {
    is Literal  -> p.version
    is Operator -> p.version + p.data.sumOf(::sumOfVersions)
}

private fun eval(p: Packet): Long = when (p) {
    is Literal  -> p.data
    is Operator -> when (p.type) {
        0    -> p.data.sumOf(::eval)
        1    -> p.data.map(::eval).reduce(Long::times)
        2    -> p.data.minOf(::eval)
        3    -> p.data.maxOf(::eval)
        5    -> p.data.map(::eval).let { if (it[0] > it[1]) 1L else 0L }
        6    -> p.data.map(::eval).let { if (it[0] < it[1]) 1L else 0L }
        7    -> p.data.map(::eval).let { if (it[0] == it[1]) 1L else 0L }
        else -> throw IllegalStateException()
    }
}