package adventofcode.y2016

import adventofcode.io.AdventSolution
import adventofcode.util.collections.firstDuplicate
import adventofcode.util.collections.onlyChanges
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2

object Day01 : AdventSolution(2016, 1, "No Time for a Taxicab") {

    override fun solvePartOne(input: String) = parseCommands(input)
        .fold(Person(Direction.UP, Vec2.origin), Person::applyCommand)
        .position
        .distance(Vec2.origin)

    override fun solvePartTwo(input: String) = parseCommands(input)
        .flatMap(Command::decompose)
        .scan(Person(Direction.UP, Vec2.origin), Person::applyCommand)
        .map(Person::position)
        .onlyChanges()
        .firstDuplicate()!!
        .distance(Vec2.origin)

    private fun parseCommands(input: String) = input
        .splitToSequence(", ")
        .flatMap { listOf(it.substring(0, 1), it.substring(1)) }
        .map(String::toCommand)
}


private sealed class Command {

    abstract fun apply(p: Person): Person
    open fun decompose() = listOf(this)

    data class Forward(val distance: Int) : Command() {
        override fun apply(p: Person) = p.copy(position = p.position + p.direction.vector * distance)
        override fun decompose() = List(distance) { SingleStep }
    }

    object Left : Command() {
        override fun apply(p: Person) = p.copy(direction = p.direction.turnLeft)
    }

    object Right : Command() {
        override fun apply(p: Person) = p.copy(direction = p.direction.turnRight)
    }

    object SingleStep : Command() {
        override fun apply(p: Person) = p.copy(position = p.position + p.direction.vector)
    }
}

private fun String.toCommand() = when (this) {
    "L" -> Command.Left
    "R" -> Command.Right
    "1" -> Command.SingleStep
    else -> Command.Forward(toInt())
}

private data class Person(val direction: Direction, val position: Vec2) {
    fun applyCommand(c: Command) = c.apply(this)
}
