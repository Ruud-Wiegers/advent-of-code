package adventofcode.y2015

import adventofcode.AdventSolution
import java.math.BigInteger

object Day25 : AdventSolution(2015, 25, "Let It Snow")
{

    override fun solvePartOne(input: String): BigInteger
    {
        val (row, column) = readPosition(input)
        val index = convertToIndex(row, column)

        val seed = 20151125.toBigInteger()
        val mult = 252533.toBigInteger()
        val mod = 33554393.toBigInteger()
        return mult.modPow(index.toBigInteger(), mod) * seed % mod
    }

    private fun readPosition(input: String): Pair<Int, Int>
    {
        val row = input.substringAfter("row ").substringBefore(",").toInt()
        val column = input.substringAfter("column ").substringBefore(".").toInt()
        return row to column
    }

    private fun convertToIndex(row: Int, column: Int): Int = triangle(row + column - 1) - row
    private fun triangle(n: Int) = (n * (n + 1)) / 2

    override fun solvePartTwo(input: String) = "Gratis!"
}



