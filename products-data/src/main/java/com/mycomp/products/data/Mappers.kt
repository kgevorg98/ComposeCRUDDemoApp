package com.mycomp.products.data

import com.mycomp.products.data.models.Product
import com.mycomp.products.database.models.ProductEntity

fun ProductEntity.toProduct(): Product =
    Product(
        id = this.id,
        name = this.name,
        time = this.time,
        tags = this.tags,
        amount = this.amount
    )