package com.example.nodechain.ui.chain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.nodechain.domain.chain.NodeRepository
import com.example.nodechain.domain.model.Node
import kotlinx.coroutines.Dispatchers

class NodeViewModel(private val repository: NodeRepository) : ViewModel() {

    val rootNode = liveData(Dispatchers.IO) {
        emit(repository.getRootNode()) // Получаем корневой узел
    }

    fun addChildToNode(parent: Node) {
        repository.addChildNode(parent)
    }

    fun removeNode(node: Node) {
        repository.removeNode(node)
    }

    fun saveNodeState(node: Node) {
        repository.saveNodeState(node)
    }

    fun loadNodeState() {
        repository.loadNodeState()
    }
}
