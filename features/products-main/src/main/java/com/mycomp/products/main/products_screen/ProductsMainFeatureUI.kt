package com.mycomp.products.main.products_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mycomp.products.main.models.ProductUi
import com.mycomp.products.main.products_screen.ProductMainScreenContract.Event
import com.mycomp.products.main.products_screen.composables.ProductItem
import com.mycomp.products.main.products_screen.composables.SearchSection

@Composable
fun ProductsMainScreen() {
    ProductsMainScreen(viewModel = viewModel())
}

@Composable
internal fun ProductsMainScreen(viewModel: ProductsViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        SearchSection(
            onSearch = { viewModel.setEvent(Event.SearchProductsByName(it)) }
        )
        when {
            state.isLoading -> {
                LoadingSection()
            }

            state.error != null -> {
                Text(text = "Error: ${state.error}")
            }

            else -> {
                MainContent(
                    products = state.products,
                    onChangeAmountClick = { productId, amount ->
                        viewModel.setEvent(
                            Event.ChangeProductsAmount(
                                productId = productId,
                                amount = amount
                            )
                        )
                    },
                    onRemoveProductClick = { productId ->
                        viewModel.setEvent(
                            Event.RemoveProduct(productId = productId)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    products: List<ProductUi>,
    onChangeAmountClick: (Int, Int) -> Unit,
    onRemoveProductClick: (Int) -> Unit
) {
    LazyColumn {
        items(products) { product ->
            ProductItem(
                product = product,
                onChangeAmountClick = { amount ->
                    onChangeAmountClick(product.id, amount)
                },
                onRemoveProductClick = {
                    onRemoveProductClick(product.id)
                }
            )
        }
    }
}


@Composable
fun LoadingSection() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
