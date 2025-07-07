package com.example.storemanager.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.storemanager.db.StoreConverter

@Entity(tableName = "Item")
@TypeConverters(StoreConverter::class)
data class Item (
    @PrimaryKey(autoGenerate = true)
    val itemId : Long,
    val itemName : String,
    val quantityPerItem : String,
    val itemPrice : Double,
    val barcode : String
)
