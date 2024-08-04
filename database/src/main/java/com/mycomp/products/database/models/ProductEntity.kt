package com.mycomp.products.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("time") val time: Long,
    @ColumnInfo("tags") val tags: List<String>,
    @ColumnInfo("amount") val amount: Int
)