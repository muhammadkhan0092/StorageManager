package com.example.storemanager.data

import androidx.room.Embedded

data class PurchasedItemDetail(
    @Embedded val item: Item,
    val quantity: Int,
)
