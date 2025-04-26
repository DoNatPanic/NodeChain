package com.example.nodechain.domain.common

import com.example.nodechain.domain.model.Node

sealed interface SearchResult {

    data object Empty : SearchResult

    data class NodeContent(
        val nodesCount: Int,
        val nodes: List<Node>
    ) : SearchResult
}