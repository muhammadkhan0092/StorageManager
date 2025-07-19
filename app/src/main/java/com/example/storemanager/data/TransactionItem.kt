package com.example.storemanager.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Transaction::class,
            parentColumns = ["transactionId"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionItem(
    @PrimaryKey(autoGenerate = true)
    val transactionItemId: Long,
    val transactionId: Long,
    val quantity: Int,
    val itemId : Long,
    val itemPrice : Double,
    val itemName : String,
    val quantityPerItem : String
)
