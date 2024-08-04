package com.mycomp.products.data

import com.mycomp.products.data.models.Product
import com.mycomp.products.database.ProductsDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class ProductsRepository @Inject constructor(
    private val database: ProductsDatabase
) {


    fun getAllProducts(): Flow<Result<List<Product>>> {
        return database.productDao.getAllProducts()
            .map { products ->
                Result.Success(products.map { it.toProduct() }) as Result<List<Product>>
            }
            .catch { e -> emit(Result.Error(e)) }
    }

    suspend fun changeProductAmount(productId: Int, newAmount: Int): Result<Unit> {
        return try {
            val product = database.productDao.getProductById(productId)
            if (product != null) {
                val updatedProduct = product.copy(amount = newAmount)
                val rowsUpdated = database.productDao.updateProduct(updatedProduct)
                if (rowsUpdated > 0) {
                    Result.Success(Unit)
                } else {
                    Result.Error(Exception("Update failed"))
                }
            } else {
                Result.Error(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun removeProductById(productId: Int): Result<Unit> {
        return try {
            database.productDao.deleteProductById(productId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun searchProductsByName(name: String): Flow<Result<List<Product>>> {
        return database.productDao.searchProductsByName(name)
            .map { products ->
                Result.Success(products.map { it.toProduct() }) as Result<List<Product>>
            }.onStart {
                emit(Result.Loading)
            }
            .catch { e -> emit(Result.Error(e)) }
    }
}
