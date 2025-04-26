package com.example.nodechain.domain.chain

import com.example.nodechain.domain.model.Node

interface NodeRepository {
    fun getRoot(): Node
    fun addNode(parentId: String): Node
    fun removeNode(id: String)
}