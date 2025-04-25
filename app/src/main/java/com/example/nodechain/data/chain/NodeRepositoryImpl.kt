package com.example.nodechain.data.chain

import android.content.Context
import android.content.SharedPreferences
import com.example.nodechain.data.sharedprefs.CHAIN
import com.example.nodechain.data.sharedprefs.SharedPrefs
import com.example.nodechain.domain.chain.NodeRepository
import com.example.nodechain.domain.model.Node
import com.google.gson.Gson

class NodeRepositoryImpl(
    private val sharedPrefs: SharedPrefs,
    private val gson: Gson,
): NodeRepository {
    private fun createJsonFromNode(node: Node): String {
        return gson.toJson(node)
    }

    // Сохранение состояния дерева
    override fun saveNodeState(node: Node) {
        val json = createJsonFromNode(node)
        sharedPrefs.saveNodeState(json)
    }

    // Загрузка состояния дерева
    override fun loadNodeState(): Node? {
        val json = sharedPrefs.loadNodeState()
        return gson.fromJson(json, Node::class.java)
    }

    // Получаем корневой узел
    override fun getRootNode(): Node {
        return loadNodeState() ?: Node(name = "root") // Если нет сохраненного дерева, создаем корень
    }

    // Добавление дочернего узла
    override fun addChildNode(parent: Node) {
        val child = Node(name = "child_${parent.children.size + 1}", parent = parent)
        parent.children.add(child)

        // TODO

        saveNodeState(parent) // Сохраняем состояние дерева
    }

    // Удаление узла
    override fun removeNode(node: Node) {
        node.parent?.children?.remove(node)
        saveNodeState(node.parent ?: node) // Сохраняем дерево после удаления
    }
}
