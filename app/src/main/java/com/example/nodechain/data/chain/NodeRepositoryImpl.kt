package com.example.nodechain.data.chain

import com.example.nodechain.data.sharedprefs.SharedPrefs
import com.example.nodechain.domain.chain.NodeRepository
import com.example.nodechain.domain.model.Node
import com.example.nodechain.domain.model.generateNodeId
import com.google.gson.Gson
import java.util.UUID

class NodeRepositoryImpl(
    private val sharedPrefs: SharedPrefs,
    private val gson: Gson,
) : NodeRepository {
    private var root: Node = loadNodeState() ?: Node(id = generateNodeId("root"), parentId = null)

    override fun getRoot(): Node = root

    override fun addNode(parentId: String): Node {
        val parent = findNode(root, parentId)
        val newNode = Node(id = generateNodeId(UUID.randomUUID().toString()), parentId = parentId)
        parent?.children?.add(newNode)
        saveNodeState(root)
        return newNode
    }

    override fun removeNode(id: String) {
        removeNodeRecursive(root, id)
        saveNodeState(root)
    }

    // Сохранение состояния дерева
    private fun saveNodeState(node: Node) {
        val json = createJsonFromNode(node)
        sharedPrefs.saveNodeState(json)
    }

    // Загрузка состояния дерева
    private fun loadNodeState(): Node? {
        val json = sharedPrefs.loadNodeState()
        return gson.fromJson(json, Node::class.java)
    }

    private fun createJsonFromNode(node: Node): String {
        return gson.toJson(node)
    }

    private fun findNode(node: Node, id: String): Node? {
        if (node.id == id) return node
        for (child in node.children) {
            val result = findNode(child, id)
            if (result != null) return result
        }
        return null
    }

    private fun removeNodeRecursive(parent: Node, id: String): Boolean {
        val iterator = parent.children.iterator()
        while (iterator.hasNext()) {
            val child = iterator.next()
            if (child.id == id) {
                iterator.remove()
                return true
            } else if (removeNodeRecursive(child, id)) return true
        }
        return false
    }
}
