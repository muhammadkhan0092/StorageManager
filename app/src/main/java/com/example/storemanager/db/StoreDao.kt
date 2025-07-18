package com.example.storemanager.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.storemanager.data.Item
import com.example.storemanager.data.ItemSalesSummary
import com.example.storemanager.data.Transaction
import com.example.storemanager.data.TransactionDetails
import com.example.storemanager.data.TransactionItem
import com.example.storemanager.data.TransactionWithUser
import com.example.storemanager.data.User

@Dao
interface StoreDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateItem(item: Item) : Int


    @Insert
    suspend fun insertItem(item: Item): Long

    @Delete
    suspend fun deleteItem(item: Item) : Int

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

    @androidx.room.Transaction
    @Query("SELECT * FROM `Transaction` WHERE userNo LIKE '%' || :mobileNo || '%' ORDER BY data DESC ")
    fun getTransactionWithSpecificUser(mobileNo : String) : PagingSource<Int, TransactionWithUser>

    @Query("SELECT * FROM `TRANSACTION` WHERE transactionId=:transactionId")
    suspend fun getTransactionItemsWithUser(transactionId: Long): TransactionDetails

    @Query("""
        SELECT 
            t.itemId  AS id,
            t.itemName AS name,
            t.itemPrice AS price,
            SUM(t.quantity) AS totalQuantity
        FROM `TransactionItem` t
        GROUP BY t.itemId
        ORDER BY totalQuantity DESC
    """)
    suspend fun getItemSalesSummary(): List<ItemSalesSummary>




}