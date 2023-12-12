package adventofcode.y2023

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class Day12Test {

    @Test
    fun solvePartOne_a() =  assertEquals(1, Day12.solvePartOne(".???.### 1,1,3"))

    @Test
    fun solvePartOne_b() =    assertEquals(4, Day12.solvePartOne(".??..??...?##. 1,1,3"))
    @Test
    fun solvePartOne_c() =     assertEquals(1, Day12.solvePartOne("?#?#?#?#?#?#?#? 1,3,1,6"))
    @Test
    fun solvePartOne_d() =     assertEquals(1, Day12.solvePartOne("????.#...#... 4,1,1"))
    @Test
    fun solvePartOne_e() =    assertEquals(4, Day12.solvePartOne("????.######..#####. 1,6,5"))
    @Test
    fun solvePartOne_f() =     assertEquals(10, Day12.solvePartOne("?###???????? 3,2,1"))


    @Test
    fun solvePartTwo() {
        assertEquals(525152, Day12.solvePartTwo("""???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1"""))
    }
}




