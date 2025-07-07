package com.example.storemanager.data

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithUser(
    @Embedded val transaction: Transaction,

    @Relation(
        parentColumn = "userNo",
        entityColumn = "userNo"
    )
    val user: User
)
