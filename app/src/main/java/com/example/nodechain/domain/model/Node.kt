package com.example.nodechain.domain.model

import java.security.MessageDigest

data class Node(
    val id: String,
    val parentId: String?, // null for root
    val children: MutableList<Node> = mutableListOf()
)

fun generateNodeId(data: String): String {
    val hash = MessageDigest.getInstance("SHA-256").digest(data.toByteArray())
    return hash.takeLast(20).joinToString("") { "%02x".format(it) }
}