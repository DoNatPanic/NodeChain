package com.example.nodechain.ui.chain.fragment

import androidx.recyclerview.widget.RecyclerView
import com.example.nodechain.databinding.NodeViewBinding
import com.example.nodechain.domain.model.Node

class NodeViewHolder(
    private val binding: NodeViewBinding,
    private val onClick: (id: String) -> Unit,
    private val onDeleteClick: (id: String) -> Boolean = { _ -> false }
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Node) {
        binding.nodeId.text = model.id

        binding.root.setOnClickListener { _ -> onClick(model.id) }
        binding.root.setOnLongClickListener { _ -> onDeleteClick(model.id) }
    }
}