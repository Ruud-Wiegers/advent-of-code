package adventofcode.util

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

private val instance = MessageDigest.getInstance("MD5")

fun md5(str: String): String = instance
		.apply { update(str.toByteArray()) }
		.digest()
		.let { DatatypeConverter.printHexBinary(it) }
		.toLowerCase()