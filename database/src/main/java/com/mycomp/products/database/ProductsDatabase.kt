package com.mycomp.products.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mycomp.products.database.dao.ProductDao
import com.mycomp.products.database.models.ProductEntity
import com.mycomp.products.database.utils.ProductConverters
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

fun copyDatabase(context: Context, databaseName: String) {
    val dbPath = context.getDatabasePath(databaseName)
    if (dbPath.exists()) return

    dbPath.parentFile?.mkdirs()
    val inputStream: InputStream = context.assets.open("database/$databaseName")
    val outputStream: OutputStream = FileOutputStream(dbPath)

    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) {
        outputStream.write(buffer, 0, length)
    }

    outputStream.flush()
    outputStream.close()
    inputStream.close()

    dbPath.setWritable(true)
}

class ProductsDatabase internal constructor(private val database: ProductsRoomDatabase) {
    val productDao: ProductDao
        get() = database.productDao()
}

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
@TypeConverters(ProductConverters::class)
internal abstract class ProductsRoomDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

fun ProductsDatabase(applicationContext: Context): ProductsDatabase {
    copyDatabase(applicationContext, "products.db")
    val productsRoomDatabase =
        Room.databaseBuilder(
            checkNotNull(applicationContext.applicationContext),
            ProductsRoomDatabase::class.java,
            applicationContext.getDatabasePath("products.db").absolutePath
        ).build()
    return ProductsDatabase(productsRoomDatabase)
}