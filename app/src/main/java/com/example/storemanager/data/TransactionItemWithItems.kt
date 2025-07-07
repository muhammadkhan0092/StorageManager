package com.example.storemanager.data

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionItemWithItems(
    @Embedded
    val transactionItem: TransactionItem,

    @Relation(
        entityColumn = "itemId",
        parentColumn = "itemId"
    )
    val item: Item
)
