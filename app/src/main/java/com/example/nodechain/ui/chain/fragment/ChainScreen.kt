import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.nodechain.domain.common.SearchResult
import com.example.nodechain.domain.model.Node
import com.example.nodechain.ui.chain.viewmodel.ChainViewModel
import androidx.compose.foundation.lazy.items

//@Composable
//fun HelloScreen() {
//    Text(text = "Привет, Compose!")
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChainScreen(
    nodeId: String?,
    viewModel: ChainViewModel = viewModel(),
    onBack: () -> Unit,
    onOpenNode: (String) -> Unit,
) {
    val context = LocalContext.current
    var nodeToDelete by remember { mutableStateOf<Node?>(null) }

    val nodes by viewModel.loadedChainLiveData().observeAsState(SearchResult.Empty)

    LaunchedEffect(nodeId) {
        viewModel.loadNodes(nodeId)
    }

    val title = if (nodeId == null) "Корневая директория" else "Узел $nodeId"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon =
                {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = { viewModel.addChild(nodeId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Создать узел")
            }
        }
    ) { innerPadding ->
        when (nodes) {
            is SearchResult.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет узлов")
                }
            }

            is SearchResult.NodeContent -> {
                val nodeList = (nodes as SearchResult.NodeContent).nodes
                LazyColumn(
                    contentPadding = innerPadding,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(nodeList) { node ->
                        NodeItem(
                            node = node,
                            onClick = { viewModel.onNodeClicked(node.id) },
                            onDelete = {
                                nodeToDelete = node
                            }
                        )
                    }
                }
            }
        }
    }

    if (nodeToDelete != null) {
        AlertDialog(
            onDismissRequest = { nodeToDelete = null },
            title = { Text("Подтверждение удаления") },
            text = { Text("Вы действительно хотите удалить элемент?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.removeNode(nodeToDelete!!.id, nodeId)
                        nodeToDelete = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { nodeToDelete = null }) {
                    Text("Отмена")
                }
            }
        )
    }


    // навигация
    val openNodeId by viewModel.getOpenNodeTrigger().observeAsState()
    openNodeId?.let {
        onOpenNode(it)
    }
}

@Composable
fun NodeItem(
    node: Node,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = node.id, modifier = Modifier.weight(1f))

        IconButton(onClick = { onDelete() }) {
            Icon(Icons.Default.Delete, contentDescription = "Удалить")
        }
    }
}

@Composable
fun NodeNavHost(startNodeId: String? = null) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "chain") {
        composable("chain?nodeId={nodeId}",
            arguments = listOf(
                navArgument("nodeId") {
                    nullable = true
                    defaultValue = startNodeId
                }
            )
        ) { backStackEntry ->
            val nodeId = backStackEntry.arguments?.getString("nodeId")
            ChainScreen(
                nodeId = nodeId,
                onBack = { navController.popBackStack() },
                onOpenNode = { newId ->
                    navController.navigate("chain?nodeId=$newId")
                }
            )
        }
    }
}



