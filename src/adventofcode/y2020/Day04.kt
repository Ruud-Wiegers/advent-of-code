package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day04.solve()

object Day04 : AdventSolution(2020, 4, "Passport Processing")
{
    override fun solvePartOne(input: String) =
        input.splitToSequence("\n\n")
            .map(::parseInput)
            .count(Document::hasRequiredFields)

    override fun solvePartTwo(input: String) =
        input.splitToSequence("\n\n")
            .map(::parseInput)
            .count(Document::isCorrectlyFormatted)

    private fun parseInput(input: String) = input
        .splitToSequence(' ', '\n')
        .map { it.split(':') }
        .associate { (k, v) -> k to v }
        .let(::Document)

    class Document(private val map: Map<String, String>)
    {

        fun hasRequiredFields() = listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
            .all(map::containsKey)

        fun isCorrectlyFormatted() = sequenceOf(
            { map["byr"]?.toIntOrNull() ?: 0 in 1920..2002 },
            { map["iyr"]?.toIntOrNull() ?: 0 in 2010..2020 },
            { map["eyr"]?.toIntOrNull() ?: 0 in 2020..2030 },
            {
                val hgt = map.getOrDefault("hgt", "")
                when (hgt.takeLast(2))
                {
                    "cm" -> hgt.dropLast(2).toIntOrNull() ?: 0 in 150..193
                    "in" -> hgt.dropLast(2).toIntOrNull() ?: 0 in 59..76
                    else -> false
                }
            },
            { map.getOrDefault("hcl", "").let(hclValidator::matches) },
            { map.getOrDefault("ecl", "").let(eclValidator::matches) },
            { map.getOrDefault("pid", "").let(pidValidator::matches) }
        )
            .all { validator -> validator() }

        private val hclValidator = "#[a-f0-9]{6}".toRegex()
        private val eclValidator = "amb|blu|brn|gry|grn|hzl|oth".toRegex()
        private val pidValidator = "[0-9]{9}".toRegex()
    }
}
