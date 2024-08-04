package com.mycomp.products.main

import com.mycomp.products.data.models.Product
import com.mycomp.products.main.models.ProductUi

internal fun Product.toUiProduct(): ProductUi = ProductUi(
    id = this.id,
    name = this.name,
    time = this.time,
    tags = this.tags,
    amount = this.amount
)