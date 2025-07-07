package com.example.storemanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.storemanager.data.Item
import com.example.storemanager.data.Transaction
import com.example.storemanager.data.TransactionItem
import com.example.storemanager.data.User


@Database([Item::class, User::class, Transaction::class, TransactionItem::class], version = 1)
@TypeConverters(StoreConverter::class)
abstract class StoreDatabase : RoomDatabase() {
    abstract fun getStoreDao() : StoreDao
}