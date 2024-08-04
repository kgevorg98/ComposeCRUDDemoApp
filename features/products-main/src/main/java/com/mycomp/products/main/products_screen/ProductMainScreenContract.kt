package com.mycomp.products.main.products_screen

import com.mycomp.products.main.models.ProductUi

object ProductMainScreenContract {
    sealed class Event {
        data class ChangeProductsAmount(val productId: Int, val amount: Int) : Event()
        data class RemoveProduct(val productId: Int) : Event()
        data class SearchProductsByName(val name:String):Event()
    }

    data class State(
        val products: List<ProductUi> = emptyList(),
        val isLoading: Boolean = true,
        val error: String? = null
    )
}