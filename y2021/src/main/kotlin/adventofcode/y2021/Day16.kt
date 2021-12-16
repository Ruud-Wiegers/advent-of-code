package adventofcode.y2021

import adventofcode.AdventSolution

object Day16 : AdventSolution(2021, 16, "Packet Decoder") {
    override fun solvePartOne(input: String): Long = PacketDecoder(input).parsePacket().let(::sumOfVersions)
    override fun solvePartTwo(input: String): Long = PacketDecoder(input).parsePacket().let(::eval)
}

private sealed class Packet(val version: Int, val type: Int)
private class Literal(version: Int, type: Int, val data: Long) : Packet(version, type)
private class Operator(version: Int, type: Int, val data: List<Packet>) : Packet(version, type)

private class PacketDecoder(input: String) {
    private val binary: String = input.toBigInteger(16).toString(2)
    private var mark = 0

    private fun readBits(i: Int): Int {
        val result = binary.substring(mark, mark + i).toInt(2)
        mark += i
        return result
    }

    fun parsePacket(): Packet {
        val version = readBits(3)
        val type = readBits(3)
        return when (type) {
            4    -> Literal(version, type, parseLiteral())
            else -> Operator(version, type, parseSubpackets())
        }
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
            val lastBit = readBits(15) + mark
            buildList {
                while (mark < lastBit) {
                    add(parsePacket())
                }
            }
        } else {
            val packetCount = readBits(11)
            List(packetCount) { parsePacket() }
        }
    }
}

private fun sumOfVersions(p: Packet): Long = when (p) {
    is Literal  -> p.version.toLong()
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