package com.mycomp.products.main.products_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mycomp.products.data.Result
import com.mycomp.products.main.models.ProductUi
import com.mycomp.products.main.products_screen.ProductMainScreenContract.Event
import com.mycomp.products.main.products_screen.ProductMainScreenContract.Event.ChangeProductsAmount
import com.mycomp.products.main.products_screen.ProductMainScreenContract.State
import com.mycomp.products.main.usecases.ChangeProductAmountUseCase
import com.mycomp.products.main.usecases.GetAllProductsUseCase
import com.mycomp.products.main.usecases.RemoveProductByIdUseCase
import com.mycomp.products.main.usecases.SearchProductsByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class ProductsViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val changeProductAmountUseCase: ChangeProductAmountUseCase,
    private val removeProductByIdUseCase: RemoveProductByIdUseCase,
    private val searchProductsByNameUseCase: SearchProductsByNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    private fun Event.handleEvent() {
        when (this) {
            is ChangeProductsAmount -> changeProductAmount(productId = productId, amount = amount)
            is Event.RemoveProduct -> removeProductById(productId = productId)
            is Event.SearchProductsByName -> searchProductByName(name = name)
            else -> {}
        }
    }

    private fun changeProductAmount(productId: Int, amount: Int) {
        viewModelScope.launch {
            changeProductAmountUseCase(productId = productId, newAmount = amount)
        }
    }

    private fun removeProductById(productId: Int) {
        viewModelScope.launch {
            removeProductByIdUseCase(productId = productId)
        }
    }

    private fun searchProductByName(name: String) {
        viewModelScope.launch {
            setState { it.copy(isLoading = true) }
            searchProductsByNameUseCase(name).collect { result ->
                getProducts(result)
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            setState { it.copy(isLoading = true) }
            getAllProductsUseCase().collect { result ->
                getProducts(result)
            }
        }
    }

    private fun getProducts(result: Result<List<ProductUi>>) {
        when (result) {
            is Result.Success -> setState {
                it.copy(
                    products = result.data,
                    isLoading = false
                )
            }

            is Result.Error -> setState {
                it.copy(
                    error = result.exception.localizedMessage,
                    isLoading = false
                )
            }

            is Result.Loading -> setState { it.copy(isLoading = true) }
        }
    }


    private fun setState(block: (State) -> State) {
        _uiState.update {
            block(it)
        }
    }

    fun setEvent(event: Event) {
        event.handleEvent()
    }
}
