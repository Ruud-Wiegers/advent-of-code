package adventofcode.y2022

import adventofcode.io.AdventSolution
import adventofcode.util.collections.cycle
import adventofcode.util.collections.takeWhileDistinct
import adventofcode.util.vector.Direction
import adventofcode.util.vector.Vec2


object Day17 : AdventSolution(2022, 17, "Pyroclastic Flow") {

    override fun solvePartOne(input: String): Int {

        val jets: Iterator<Vec2> = input
            .map { if (it == '<') Direction.LEFT.vector else Direction.RIGHT.vector }
            .cycle()
            .iterator()

        val blocks = Tetris.values()
            .map(Tetris::toPoints)
            .cycle()
            .take(2022)

        val playingField = (0..6).map { Vec2(it, 0) }.toMutableSet()


        for (block: Set<Vec2> in blocks) {
            val floor = playingField.maxOf { it.y }

            var positionedBlock = block.map { it + Vec2(2, floor + 4) }

            while (true) {
                val moveSideways = jets.next().let { push -> positionedBlock.map { it + push } }
                if (moveSideways.all { it.x in 0..6 } && moveSideways.none { it in playingField }) {
                    positionedBlock = moveSideways
                }

                val moveDown = positionedBlock.map { it + Direction.UP.vector }
                if (moveDown.none { it in playingField }) {
                    positionedBlock = moveDown
                } else {
                    playingField += positionedBlock
                    break
                }
            }

        }
        return playingField.maxOf { it.y }
    }

    override fun solvePartTwo(input: String): Long {
        val jets: List<Vec2> = input
            .map { if (it == '<') Direction.LEFT.vector else Direction.RIGHT.vector }


        val blocks = Tetris.values().map(Tetris::toPoints)

        val playingField = (0..6).map { Vec2(it, 0) }


        val seq = generateSequence(State(0, 0, 0, playingField.toSet())) { state ->
            buildNextState(state, blocks, jets)
        }
            .takeWhileDistinct()
            .toList()

        val blocksToDrop = 1_000_000_000_000

        var floors = seq.sumOf { it.shifted }.toLong()

        val cycle = generateSequence(buildNextState(seq.last(), blocks, jets)) {
            buildNextState(it, blocks, jets)
        }.takeWhileDistinct().toList()


        val cycleCount = (blocksToDrop - seq.size) / cycle.size

        floors += cycle.sumOf { it.shifted } * cycleCount

        val rem = generateSequence(buildNextState(cycle.last(), blocks, jets)) { buildNextState(it, blocks, jets) }
            .take(((blocksToDrop - seq.size) % cycle.size).toInt() + 1).toList()


        floors += rem.sumOf { it.shifted }
        floors += rem.last().field.maxOf { it.y }
        return floors
    }

    private fun buildNextState(state: State, blocks: List<Set<Vec2>>, jets: List<Vec2>): State {
        var jetPos = state.jet

        val floor = state.field.maxOf { it.y }
        var positionedBlock = blocks[state.block].map { it + Vec2(2, floor + 4) }

        while (true) {
            val moveSideways = jets[jetPos++].let { push -> positionedBlock.map { it + push } }
            jetPos %= jets.size
            if (moveSideways.all { it.x in 0..6 } && moveSideways.none { it in state.field }) {
                positionedBlock = moveSideways
            }

            val moveDown = positionedBlock.map { it + Direction.UP.vector }
            if (moveDown.none { it in state.field }) {
                positionedBlock = moveDown
            } else {
                break
            }
        }

        val newField = (state.field + positionedBlock)
        val shift = (newField.maxOf { it.y } - 50).coerceAtLeast(0)
        val clippedField = newField.map { it - Vec2(0, shift) }.filter { it.y >= 0 }


        return State(jetPos, (state.block + 1) % blocks.size, shift, clippedField.toSet())
    }

    private data class State(val jet: Int, val block: Int, val shifted: Int, val field: Set<Vec2>)


    private enum class Tetris(val blocks: List<List<Boolean>>) {
        Line("####"),
        Plus(" # \n###\n # "),
        Hook("  #\n  #\n###"),
        Beam("#\n#\n#\n#"),
        Square("##\n##");

        constructor(string: String) : this(string.lines().reversed().map { it.map { it == '#' } })

        fun toPoints() = blocks.flatMapIndexed { y, row ->
            row.mapIndexed { x, isSolid -> Vec2(x, y).takeIf { isSolid } }
        }.filterNotNull().toSet()
    }
}