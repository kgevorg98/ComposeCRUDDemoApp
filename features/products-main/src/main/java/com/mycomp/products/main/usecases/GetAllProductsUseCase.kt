package com.mycomp.products.main.usecases

import com.mycomp.products.data.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.mycomp.products.data.Result
import com.mycomp.products.main.models.ProductUi
import com.mycomp.products.main.toUiProduct

internal class GetAllProductsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {
    operator fun invoke(): Flow<Result<List<ProductUi>>> {
        return productsRepository.getAllProducts()
            .map { result ->
                when (result) {
                    is Result.Success -> Result.Success(result.data.map { it.toUiProduct() })
                    is Result.Error -> Result.Error(result.exception)
                    is Result.Loading -> Result.Loading
                }
            }
    }
}