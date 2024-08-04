package com.mycomp.products.main.usecases

import com.mycomp.products.data.ProductsRepository
import com.mycomp.products.data.Result
import javax.inject.Inject

internal class ChangeProductAmountUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(productId: Int, newAmount: Int): Result<Unit> {
        return repository.changeProductAmount(productId, newAmount)
    }
}