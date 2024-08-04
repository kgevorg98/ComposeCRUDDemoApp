package com.mycomp.products.data.models

data class Product(
    val id: Int = 0,
    val name: String,
    val time: Long,
    val tags: List<String>,
    val amount: Int
)