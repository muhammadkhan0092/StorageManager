package com.example.storemanager.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userNo"],
            childColumns = ["userNo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId : Long,
    val userNo : String,
    val data : Long = System.currentTimeMillis(),
    val totalAmount : Double
)
