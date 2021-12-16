package adventofcode.y2021

import adventofcode.AdventSolution
import adventofcode.solve

fun main() {
    Day16.solve()
}

object Day16 : AdventSolution(2021, 16, "BITS") {
    override fun solvePartOne(input: String): Long {
        val iter = BitsReader(input)
        val p = parsePacket(iter)

        fun versions(p: Packet): Long = when (p) {
            is Literal -> p.version.toLong()
            is Operator -> p.version + p.data.sumOf { versions(it) }
        }

        return versions(p)
    }

    override fun solvePartTwo(input: String): Long {
        val iter = BitsReader(input)
        val p = parsePacket(iter)

        fun eval(p: Packet): Long = when (p) {
            is Literal -> p.data
            is Operator -> when (p.type) {
                0 -> p.data.sumOf(::eval)
                1 -> p.data.map(::eval).reduce(Long::times)
                2 -> p.data.minOf(::eval)
                3 -> p.data.maxOf(::eval)
                5 -> p.data.map(::eval).let { if (it[0] > it[1]) 1L else 0L }
                6 -> p.data.map(::eval).let { if (it[0] < it[1]) 1L else 0L }
                7 -> p.data.map(::eval).let { if (it[0] == it[1]) 1L else 0L }
                else -> throw IllegalStateException()
            }
        }

        return eval(p)
    }

    private class BitsReader(input: String) {
        val binary = input
            .flatMap {
                it.toString().toInt(16).toString(2).padStart(4, '0').asIterable()
            }.joinToString("")
        var mark = 0

        fun readBits(i: Int): Int {
            val result = binary.substring(mark, mark + i).toInt(2)
            mark += i
            return result
        }

        fun readNumber(): Long {
            var result = 0L
            do {
                val next = readBits(1) == 1
                result *= 16
                result += readBits(4)
            } while (next)
            return result
        }
    }

    private sealed class Packet(val version: Int, val type: Int)
    private class Literal(version: Int, type: Int, val data: Long) : Packet(version, type)
    private class Operator(version: Int, type: Int, val data: List<Packet>) : Packet(version, type)

    private fun parsePacket(reader: BitsReader): Packet {
        val version = reader.readBits(3)
        val type = reader.readBits(3)
        return when (type) {
            4 -> parseLiteral(reader, version, type)
            else -> parseOperator(reader, version, type)
        }
    }

    private fun parseLiteral(reader: BitsReader, version: Int, type: Int): Packet {
        val literal = reader.readNumber()
        return Literal(version, type, literal)
    }

    private fun parseOperator(reader: BitsReader, version: Int, type: Int): Packet {
        val lengthType = reader.readBits(1)
        return if (lengthType == 0) {
            val lastBit = reader.readBits(15) + reader.mark
            val packets = buildList {
                while (reader.mark < lastBit) {
                    add(parsePacket(reader))
                }
            }
            Operator(version, type, packets)
        } else {
            val packetCount = reader.readBits(11)
            val packets = List(packetCount) { parsePacket(reader) }
            Operator(version, type, packets)
        }


    }
}