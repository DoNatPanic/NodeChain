package com.example.nodechain.domain.chain

import com.example.nodechain.domain.model.Node

interface NodeRepository {
    fun saveNodeState(node: Node)
    fun loadNodeState(): Node?
    fun getRootNode(): Node
    fun addChildNode(parent: Node)
    fun removeNode(node: Node)
}