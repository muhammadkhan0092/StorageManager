package com.example.storemanager.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storemanager.data.Item
import com.example.storemanager.data.Transaction
import com.example.storemanager.data.TransactionDetails
import com.example.storemanager.data.TransactionItem
import com.example.storemanager.data.TransactionWithUser
import com.example.storemanager.data.User

@Dao
interface StoreDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Insert
    suspend fun insertItem(item: Item): Long

    @Insert
    suspend fun insertTransaction(transaction: Transaction): Long

    @Insert
    suspend fun insertTransactionItem(transactionItem: List<TransactionItem>)

    @Query("SELECT * FROM Item WHERE barcode =:barcode")
    suspend fun getItem(barcode : String) : Item

    @Query("SELECT * FROM Item ORDER BY itemId DESC")
    fun getDownloadingItems() : PagingSource<Int, Item>



    @Query("SELECT * FROM `Transaction`")
    suspend fun getTransactions() : List<Transaction>

    @androidx.room.Transaction
    @Query("SELECT * FROM `Transaction` ORDER BY data DESC")
    fun getTransactionWithUser() : PagingSource<Int, TransactionWithUser>

    @Query("SELECT * FROM `TRANSACTION` WHERE transactionId=:transactionId")
    suspend fun getTransactionItemsWithUser(transactionId: Long): TransactionDetails


}