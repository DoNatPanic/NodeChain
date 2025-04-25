package com.example.nodechain.domain.model

import java.security.MessageDigest

data class Node(
//    val name: String = generateNameFromHash(),
    val name: String, // Название по хэшу
    val children: MutableList<Node> = mutableListOf(),
    var parent: Node? = null
) {
//    companion object {
//        private fun generateNameFromHash(): String {
//            val randomBytes = ByteArray(32)
//            java.security.SecureRandom().nextBytes(randomBytes)
//            val digest = MessageDigest.getInstance("SHA-256")
//            val hash = digest.digest(randomBytes)
//            return "0x" + hash.takeLast(20).joinToString("") { "%02x".format(it) }
//        }
//    }
}

fun Node.generateName(): String {
    val hashBytes = MessageDigest.getInstance("SHA-256").digest(this.toString().toByteArray())
    return hashBytes.takeLast(20).joinToString("") { "%02x".format(it) }
}