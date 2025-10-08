package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinInject(),
) {
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//
//    PokemonListScreen(
//        list = uiState.success,
//        onTabSelected,
//        onItemClick = onItemClick,
//        onChangeType = { search, type, order -> viewModel.list(search, type, order) },
//        modifier,
// //    )
// }

// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// fun PokemonListScreen(
//    list: List<Pokemon>?,
//    onTabSelected: (String) -> Unit,
//    onItemClick: (String) -> Unit,
//    onChangeType: (String, String, OrderType) -> Unit,
//    modifier: Modifier = Modifier,
// ) {
//    AppContainer(
//        modifier = modifier,
//        bottomBar = AppBottomNavigation(
//            onTabSelected = onTabSelected
//        )
//    ) { modifier ->
//        Column(
//            modifier = modifier
//        ) {
//            FilterRow(onChangeType = onChangeType)
//            Spacer(modifier = Modifier.height(12.dp))
//
//            if (list.isNullOrEmpty()) {
//                Text("vazio")
//            } else {
//                LazyColumn {
//                    items(list) { pokemon ->
//                        PokemonCard(pokemon = pokemon, onItemClick)
//                        Spacer(modifier = Modifier.height(12.dp))
//                    }
//                }
//            }
//        }
//    }
// }
//
// @Composable
// fun FilterRow(
//    onChangeType: (String, String, OrderType) -> Unit,
//    modifier: Modifier = Modifier
// ) {
//    var search by remember { mutableStateOf("") }
//    var selectedType by remember { mutableStateOf("") }
//    var selectedOrder by remember { mutableStateOf(ASC) }
//
//    Column(
//        modifier = modifier
//    ) {
//        AppSearchBar(onValueChange = {
//            search = it
//            onChangeType(search, selectedType, selectedOrder)
//        })
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
//            AppSelection(
//                modifier = Modifier.weight(1f),
//                list = listOfTypes(),
//                selected = { _, selected ->
//                    selectedType = selected.value as String
//                    onChangeType(search, selectedType, selectedOrder)
//                }
//            )
//
//            AppSelection(
//                modifier = Modifier.weight(1f),
//                list = listOrder(),
//                selected = { _, selected ->
//                    selectedOrder = selected.value as OrderType
//                    onChangeType(search, selectedType, selectedOrder)
//                }
//            )
//        }
//    }
// }
//
//
// @Preview
// @Composable
// fun HomeScreenPreview() {
//    val list = listOf(
//        Pokemon("001", "Pikachu", listOf(PokemonType.ELECTRIC), "", false)
//    )
//    PokemonListScreen(list, onTabSelected = {}, onItemClick = {}, onChangeType = {_, _, _ ->})
}
