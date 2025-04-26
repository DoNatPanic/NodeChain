package com.example.nodechain.ui.chain.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nodechain.R
import com.example.nodechain.databinding.FragmentChainBinding
import com.example.nodechain.domain.common.SearchResult
import com.example.nodechain.ui.chain.viewmodel.ChainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChainFragment : Fragment() {

    private val viewModel: ChainViewModel by viewModel()

    private lateinit var nodeAdapter: NodeAdapter

    private val nodeId: String? by lazy {
        arguments?.getString("nodeId")
    }

    private lateinit var binding: FragmentChainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val owner = getViewLifecycleOwner()

        val onNodeClick: (String) -> Unit =
            { id: String -> viewModel.onNodeClicked(id) }

        val onNodeDeleteClick: (String) -> Boolean =
            { id: String ->
                showDeleteConfirmationDialog(requireContext()) {
                    viewModel.removeNode(id, nodeId)
                }
            }

        nodeAdapter = NodeAdapter(onNodeClick, onNodeDeleteClick)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = nodeAdapter

        viewModel.loadedChainLiveData().observe(owner) { result ->
            when (result) {
                is SearchResult.Empty -> {
                    nodeAdapter.submitList(listOf())
                }

                is SearchResult.NodeContent -> {
                    nodeAdapter.submitList(result.nodes)
                }
            }
        }

        binding.createNodeBtn.setOnClickListener {
            viewModel.addChild(nodeId)
        }

        viewModel.loadNodes(nodeId)

        viewModel.getOpenNodeTrigger().observe(owner) { id ->
            openNewNode(id)
        }

        // назад
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        if (nodeId == null) {
            binding.toolbar.navigationIcon = null // скрывать иконку "назад" если находимся в корне
            binding.toolbar.title = "Корневая директория"
        } else {
            binding.toolbar.title = "Узел $nodeId"
        }
    }

    private fun openNewNode(id: String) {
        val bundle = Bundle().apply {
            putString("nodeId", id)
        }
        findNavController().navigate(R.id.action_self, bundle)
    }

    private fun showDeleteConfirmationDialog(context: Context, onConfirm: () -> Unit): Boolean {
        AlertDialog.Builder(context)
            .setTitle("Подтверждение удаления")
            .setMessage("Вы действительно хотите удалить элемент?")
            .setPositiveButton("Удалить") { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
        return true
    }
}
