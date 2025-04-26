package com.example.nodechain.ui.chain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nodechain.domain.chain.NodeRepository
import com.example.nodechain.domain.common.SearchResult
import com.example.nodechain.domain.model.Node
import com.example.nodechain.ui.utils.SingleEventLiveData

class ChainViewModel(private val repository: NodeRepository) : ViewModel() {

    private val nodes = mutableListOf<Node>()

    private var loadedChainData: SingleEventLiveData<SearchResult> = SingleEventLiveData()
    fun loadedChainLiveData(): LiveData<SearchResult> = loadedChainData

    private val openNodeTrigger = SingleEventLiveData<String>()
    fun getOpenNodeTrigger(): SingleEventLiveData<String> = openNodeTrigger

    fun loadNodes(id: String?) {
        val root = repository.getRoot()
        if (id == null) {
            nodes.addAll(root.children)
        } else {
            val node = findNode(root, id)
            node?.let { nodes.addAll(it.children) }
        }
        if (nodes.size != 0) {
            loadedChainData.postValue(SearchResult.NodeContent(nodes.size, nodes))
        } else {
            loadedChainData.postValue(SearchResult.Empty)
        }
    }

    fun addChild(parentId: String?) {
        val id = parentId ?: repository.getRoot().id
        repository.addNode(id)
        nodes.clear()
        loadNodes(id)
    }

    fun removeNode(id: String, parentId: String?) {
        repository.removeNode(id)
        nodes.clear()
        loadNodes(parentId)
    }

    private fun findNode(node: Node, id: String): Node? {
        if (node.id == id) return node
        for (child in node.children) {
            findNode(child, id)?.let { return it }
        }
        return null
    }

    fun onNodeClicked(nodeId: String) {
        openNodeTrigger.postValue(nodeId)
    }
}
