package com.mycomp.products.main.models

data class ProductUi(
    val id: Int = 0,
    val name: String,
    val time: Long,
    val tags: List<String>,
    val amount: Int
)