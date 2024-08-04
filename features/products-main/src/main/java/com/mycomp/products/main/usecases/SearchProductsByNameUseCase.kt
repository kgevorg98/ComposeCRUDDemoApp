package com.mycomp.products.main.usecases

import com.mycomp.products.data.ProductsRepository
import com.mycomp.products.data.Result
import com.mycomp.products.main.models.ProductUi
import com.mycomp.products.main.toUiProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchProductsByNameUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    operator fun invoke(name:String): Flow<Result<List<ProductUi>>> {
        return productsRepository.searchProductsByName(name)
            .map { result ->
                when (result) {
                    is Result.Success -> Result.Success(result.data.map { it.toUiProduct() })
                    is Result.Error -> Result.Error(result.exception)
                    is Result.Loading -> Result.Loading
                }
            }
    }
}