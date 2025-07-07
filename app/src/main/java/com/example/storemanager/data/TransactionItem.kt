package com.example.storemanager.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["transactionId", "itemId"],
    foreignKeys = [
        ForeignKey(
            entity = Transaction::class,
            parentColumns = ["transactionId"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = ["itemId"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionItem(
    val transactionId: Long,
    val itemId: Long,
    val quantity: Int,
)
