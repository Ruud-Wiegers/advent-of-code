package adventofcode.y2022

import adventofcode.AdventSolution
import adventofcode.solve


fun main() {
    Day07.solve()
}

object Day07 : AdventSolution(2022, 7, "No Space Left On Device") {

    override fun solvePartOne(input: String): Long =
        parse(input)
            .enumerate()
            .map(Directory::size)
            .filter { it <= 100_000 }
            .sum()

    override fun solvePartTwo(input: String): Long {
        val root = parse(input)
        val required = root.size - (70_000_000 - 30_000_000)
        return root
            .enumerate()
            .map(Directory::size)
            .filter { it >= required }
            .min()
    }

    private fun parse(input: String): Directory {

        val root = Directory("/", null)
        var current = root
        input.lineSequence().forEach { line ->
            when {
                line == "$ ls" -> {}
                line == "$ cd /" -> current = root
                line == "$ cd .." -> current = current.parent!!
                line.startsWith("$ cd") -> current = current.children[line.substringAfterLast(" ")]!! as Directory
                line.startsWith("dir") -> current.addDirectory(line.substringAfterLast(' '))
                else -> current.addFile(line.substringAfterLast(' '), line.substringBefore(' ').toLong())
            }
        }
        return root
    }
}


private sealed class Node {
    abstract val name: String
    abstract val size: Long
    abstract val parent: Node?
}


private data class Directory(
    override val name: String,
    override val parent: Directory?,
    val children: MutableMap<String, Node> = mutableMapOf()
) : Node() {
    override val size: Long get() = children.values.sumOf { it.size }


    fun addFile(name: String, size: Long) {
        children[name] = (File(name, size, this))
    }

    fun addDirectory(name: String) {
        children[name] = (Directory(name, this))
    }

    fun enumerate(): List<Directory> {
        return children.values.filterIsInstance<Directory>().flatMap { it.enumerate() } + this
    }

}

private data class File(
    override val name: String,
    override val size: Long,
    override val parent: Node?
) : Node()

