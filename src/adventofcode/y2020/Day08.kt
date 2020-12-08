package adventofcode.y2020

import adventofcode.AdventSolution
import adventofcode.solve

fun main() = Day08.solve()

object Day08 : AdventSolution(2020, 8, "Handheld Halting")
{
    override fun solvePartOne(input: String): Int
    {
        val instructions = parseInstructions(input)

        return Program(0, instructions).run().first
    }

    override fun solvePartTwo(input: String): Int
    {
        val instructions = parseInstructions(input)

        return instructions.indices
            .asSequence()
            .filter { instructions[it].op != Operation.Acc }
            .map {
                val mod = instructions.toMutableList()
                mod[it]=  mod[it].copy(op = when (mod[it].op)
                {
                    Operation.Acc -> Operation.Acc
                    Operation.Nop -> Operation.Jmp
                    Operation.Jmp -> Operation.Nop
                })
                mod
            }
            .map{Program(0,it).run()}
            .first { it.second==Status.End }
            .first
    }

    private fun parseInstructions(input: String) = input.lines().map {
        val (opStr, vStr) = it.split(' ')
        val op = when (opStr)
        {
            "acc" -> Operation.Acc
            "jmp" -> Operation.Jmp
            else  -> Operation.Nop
        }
        val v = vStr.toInt()

        Instruction(op, v)
    }
}

private class Program(var acc: Int, val instructions: List<Instruction>)
{
    var pc = 0
    val visited = instructions.map { false }.toMutableList()

    fun step(): Status
    {
        if (pc !in instructions.indices) return Status.End
        if (visited[pc]) return Status.Loop
        visited[pc] = true
        val (op, v) = instructions[pc]
        when (op)
        {
            Operation.Acc -> acc += v
            Operation.Jmp -> pc += v - 1
            Operation.Nop ->
            {
            }
        }
        pc++
        return Status.Run
    }

    fun run(): Pair<Int, Status>
    {
        var status = Status.Run
        while (status == Status.Run)
        {
            status = step()
        }
        return acc to status
    }
}

private data class Instruction(val op: Operation, val v: Int)

private enum class Operation
{ Acc, Jmp, Nop }

private enum class Status
{ Run, Loop, End }