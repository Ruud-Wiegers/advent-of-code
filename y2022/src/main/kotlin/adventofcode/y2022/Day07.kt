package adventofcode.y2022

import adventofcode.AdventSolution

object Day07 : AdventSolution(2022, 7, "No Space Left On Device") {

    override fun solvePartOne(input: String): Long {
        return parse(input).values.filter { it <= 100_000 }.sum()
    }

    override fun solvePartTwo(input: String): Long {
        val directorySizes = parse(input)
        val required = directorySizes.getValue("") - 40_000_000
        return directorySizes.values.filter { it >= required }.min()
    }

    private fun parse(input: String): Map<String, Long> {
        val directories = mutableSetOf("")
        var currentPath = ""
        val files = mutableMapOf<String, Long>()

        input.lineSequence().forEach { line ->
            when {
                line == "$ ls" -> {}
                line == "$ cd /" -> currentPath = ""
                line == "$ cd .." -> currentPath = currentPath.substringBeforeLast('/')
                line.startsWith("$ cd") -> currentPath = currentPath + '/' + line.substringAfterLast(' ')
                line.startsWith("dir") -> directories += currentPath + '/' + line.substringAfterLast(' ')
                else -> files[currentPath + '/' + line.substringAfter(' ')] = line.substringBefore(' ').toLong()
            }
        }
        return directories.associateWith { dir ->
            files.asSequence().filter { it.key.startsWith(dir) }.sumOf { it.value }
        }
    }
}