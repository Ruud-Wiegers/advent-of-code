package adventofcode.util.algorithm

class UnionFind(n: Int) {
    private val parent: IntArray = IntArray(n) { it }
    private var _setCount = n
    val setCount: Int get() = _setCount

    fun union(x: Int, y: Int) {
        val xRoot = findRoot(x)
        val yRoot = findRoot(y)
        if (xRoot != yRoot) {
            parent[yRoot] = xRoot
            _setCount--
        }
    }

    fun toSets(): List<Set<Int>> {
        return parent.indices.groupBy { findRoot(it) }.values.map { it.toSet() }
    }

    private fun findRoot(x: Int): Int {
        var ancestor = x
        while (ancestor != parent[ancestor]) {
            ancestor = parent[ancestor]
        }
        parent[x] = ancestor
        return ancestor
    }
}