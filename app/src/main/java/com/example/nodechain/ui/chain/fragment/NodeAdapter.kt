package com.example.nodechain.ui.chain.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nodechain.databinding.NodeViewBinding
import com.example.nodechain.domain.model.Node

class NodeAdapter(
    private val onClick: (id: String) -> Unit,
    private val onDeleteClick: (id: String) -> Boolean
) : RecyclerView.Adapter<NodeViewHolder>() {

    private val nodes = mutableListOf<Node>()

    fun submitList(newList: List<Node>) {
        nodes.clear()
        nodes.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NodeViewHolder {
        val binding = NodeViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NodeViewHolder(binding, onClick, onDeleteClick)
    }

    override fun getItemCount(): Int {
        return nodes.size
    }

    override fun onBindViewHolder(holder: NodeViewHolder, position: Int) {
        nodes.getOrNull(position)?.let { node ->
            holder.bind(node)
        }
    }
}