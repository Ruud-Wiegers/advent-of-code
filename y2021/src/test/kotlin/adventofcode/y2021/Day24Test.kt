package adventofcode.y2021

import adventofcode.io.retrieveInput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.random.nextInt

internal class Day24Test {

    private val programString by lazy { retrieveInput(24, 2021) }
    private val fullInstructions by lazy { Day24.parse(programString) }
    private val blocks by lazy { Day24.parseDifferences(programString) }

    private val validNumbers by lazy {
        fun generate(s0: Int, s1: Int, s3: Int, s5: Int, s7: Int, s9: Int, s11: Int) =
            listOf(s0, s1, s3 + 3, s3, s11 + 8, s5, s7 + 5, s7, s9 + 2, s9, s5 + 2, s11, s1 + 3, s0 + 6)
        buildSet {
            for (s0 in 1..3)
                for (s1 in 1..6)
                    for (s3 in 1..6)
                        for (s5 in 1..7)
                            for (s7 in 1..4)
                                for (s9 in 1..7)
                                    for (s11 in 1..1)
                                        add(generate(s0, s1, s3, s5, s7, s9, s11))
        }
    }

    private fun invalidNumbers(seed: Long = 1L): Sequence<List<Int>> {
        val r = Random(seed)
        return generateSequence { List(14) { r.nextInt(1..9) } }.filter { it !in validNumbers }
    }


    @Test
    fun `full ALU program accepts all valid numbers`() {
        validNumbers.forEach {
            assertTrue(Day24.verify(fullInstructions, it.iterator()))
        }
    }

    @Test
    fun `simplified ALU program accepts all valid numbers`() {
        validNumbers.forEach {
            assertTrue(solveDecompiled(blocks, it))
        }
    }

    @Test
    fun `full ALU program rejects generated invalid numbers`() {
        invalidNumbers().take(100_000).forEach {
            assertFalse(Day24.verify(fullInstructions, it.iterator()))
        }
    }

    @Test
    fun `simplified ALU program rejects generated invalid numbers`() {
        invalidNumbers().take(100_000).forEach {
            assertFalse(solveDecompiled(blocks, it))
        }
    }

    @Test
    fun `part 1`() {
        assertEquals("36969794979199", validNumbers.maxOf { it.joinToString("") })
    }

    @Test
    fun `part 2`() {
        assertEquals("11419161313147", validNumbers.minOf { it.joinToString("") })
    }
}