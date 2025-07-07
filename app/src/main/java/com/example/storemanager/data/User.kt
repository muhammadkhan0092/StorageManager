package com.example.storemanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = false)
    val userNo : String,
    val userName : String
)
