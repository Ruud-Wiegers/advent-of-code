package adventofcode.util

import java.security.MessageDigest

private val instance = MessageDigest.getInstance("MD5")

fun md5(str: String): String = instance
		.apply { update(str.toByteArray()) }
		.digest()
		.let(::toHex)

private fun toHex(data:ByteArray): String {
	val r = StringBuilder(data.size * 2)
	for (b in data) {
		r.append(hexCode[b.toInt() shr 4 and 0xF])
		r.append(hexCode[b.toInt() and 0xF])
	}
	return r.toString()
}
private val hexCode = "0123456789abcdef".toCharArray()
