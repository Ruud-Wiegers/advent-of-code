@file:Suppress("SpellCheckingInspection")

package adventofcode.language.assembunny


fun parseToAssemBunny(string: String): List<Instruction> = string.lines().map(::parseInstruction)

private fun parseInstruction(string: String): Instruction = string.split(" ").let {
    when (it[0]) {
        "cpy" -> Cpy(parseRHS(it[1]), parseRegister(it[2]))
        "inc" -> Inc(parseRegister(it[1]))
        "dec" -> Dec(parseRegister(it[1]))
        "jnz" -> Jnz(parseRHS(it[1]), parseRHS(it[2]))
        "tgl" -> Tgl(parseRHS(it[1]))
        "out" -> Out(parseRHS(it[1]))
        else -> throw IllegalArgumentException(string)
    }
}

private fun parseRHS(s: String): RHS = when {
    s.toIntOrNull() != null -> Literal(s.toInt())
    else -> parseRegister(s)
}

private fun parseRegister(s: String): RegisterValue = when (s) {
    "a" -> RegisterValue(0)
    "b" -> RegisterValue(1)
    "c" -> RegisterValue(2)
    "d" -> RegisterValue(3)
    else -> throw IllegalArgumentException(s)
}

data class AssemBunnyContext(
    val registers: MutableList<Int>,
    var pc: Int,
    val instructionList: MutableList<Instruction>
) {
    val signal = mutableListOf<Int>()

    fun run() {
        while (pc in instructionList.indices) {
            instructionList[pc].execute(this)
        }
    }

    fun run(steps: Int) {
        repeat(steps) {
            if (pc !in instructionList.indices) return

            instructionList[pc].execute(this)
        }
    }
}


sealed class Instruction {
    abstract fun execute(context: AssemBunnyContext)
}

data class Cpy(val source: RHS, val target: RegisterValue) : Instruction() {
    override fun execute(context: AssemBunnyContext) {
        target.assign(context, source.eval(context))
        context.pc++
    }
}

data class Inc(val target: RegisterValue) : Instruction() {
    override fun execute(context: AssemBunnyContext) {
        target.assign(context, target.eval(context) + 1)
        context.pc++
    }
}

data class Dec(val target: RegisterValue) : Instruction() {
    override fun execute(context: AssemBunnyContext) {
        target.assign(context, target.eval(context) - 1)
        context.pc++
    }
}

data class Jnz(val condition: RHS, val distance: RHS) : Instruction() {
    override fun execute(context: AssemBunnyContext) {
        if (condition.eval(context) == 0)
            context.pc++
        else
            context.pc += distance.eval(context)
    }
}

data class Tgl(private val distance: RHS) : Instruction() {
    override fun execute(context: AssemBunnyContext) {
        val i = context.pc + distance.eval(context)
        if (i in context.instructionList.indices) {
            val instruction = context.instructionList[i]
            context.instructionList[i] = toggle(instruction)
        }
        context.pc++
    }

    private fun toggle(t: Instruction): Instruction = when (t) {
        is Inc -> Dec(t.target)
        is Dec -> Inc(t.target)
        is Tgl -> if (t.distance is RegisterValue) Inc(t.distance) else Nop
        is Nop -> Nop
        is Cpy -> Jnz(t.source, t.target)
        is Jnz -> if (t.distance is RegisterValue) Cpy(t.condition, t.distance) else Nop2(t.condition, t.distance)
        is Nop2 -> Jnz(t.a, t.b)
        is Out -> Out(t.a)
    }
}

object Nop : Instruction() {
    override fun execute(context: AssemBunnyContext) {
        context.pc++
    }
}

data class Nop2(val a: RHS, val b: RHS) : Instruction() {
    override fun execute(context: AssemBunnyContext) {
        context.pc++
    }
}

data class Out(val a: RHS) : Instruction() {
    override fun execute(context: AssemBunnyContext) {
        context.signal += a.eval(context)
        context.pc++
    }
}


interface RHS {
    fun eval(context: AssemBunnyContext): Int
}

interface LHS {
    fun assign(context: AssemBunnyContext, value: Int)
}

data class RegisterValue(private val register: Int) : LHS, RHS {
    override fun assign(context: AssemBunnyContext, value: Int) {
        context.registers[register] = value
    }

    override fun eval(context: AssemBunnyContext): Int = context.registers[register]
}

data class Literal(private val value: Int) : RHS {
    override fun eval(context: AssemBunnyContext): Int = value
}