package com.example.storemanager.data

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionDetails(
    @Embedded
    val transaction: Transaction,


    @Relation(
        entityColumn = "userNo",
        parentColumn = "userNo"
    )
    val user : User,

    @Relation(
        entity = TransactionItem::class,
        entityColumn = "transactionId",
        parentColumn = "transactionId"
    )
    val items : List<TransactionItem>
)
