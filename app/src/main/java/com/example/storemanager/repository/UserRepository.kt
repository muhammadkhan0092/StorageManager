package com.example.storemanager.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.example.storemanager.data.Item
import com.example.storemanager.data.Transaction
import com.example.storemanager.data.TransactionDetails
import com.example.storemanager.data.TransactionItem
import com.example.storemanager.data.TransactionWithUser
import com.example.storemanager.data.User
import com.example.storemanager.db.StoreDatabase
import com.example.storemanager.utils.OrderResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val storeDatabase: StoreDatabase) {
    suspend fun setItem(item: Item) {
        storeDatabase.getStoreDao().insertItem(item)
    }

    suspend fun getItem(barcode : String): Item {
        return storeDatabase.getStoreDao().getItem(barcode)
    }

    suspend fun updateItem(item: Item): Int {
        return storeDatabase.getStoreDao().updateItem(item)
    }

    suspend fun deleteItem(item: Item) : Int{
        return storeDatabase.getStoreDao().deleteItem(item)
    }

    fun getDownloadingItems(): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {storeDatabase.getStoreDao().getDownloadingItems()}
        ).flow
    }

    fun getUserWithTransacation(): Flow<PagingData<TransactionWithUser>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {storeDatabase.getStoreDao().getTransactionWithUser()}
        ).flow
    }

    fun getSpecificUserWithTransaction(mobileNo : String) : Flow<PagingData<TransactionWithUser>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {storeDatabase.getStoreDao().getTransactionWithSpecificUser(mobileNo)}
        ).flow
    }

    suspend fun placeOrder(
        user: User,
        transaction: Transaction,
        transactionItems: List<TransactionItem>
    ): OrderResult {
        return try {
            storeDatabase.withTransaction {
                storeDatabase.getStoreDao().insertUser(user)
                Log.d("khan","inserted user")
                val generatedId = storeDatabase.getStoreDao().insertTransaction(transaction)
                Log.d("khan","generated key is ${generatedId}")
                val updatedItems = transactionItems.map {
                    it.copy(transactionId = generatedId)
                }
                storeDatabase.getStoreDao().insertTransactionItem(updatedItems)
                Log.d("khan","inserted items as well")
            }
            OrderResult.Success
        } catch (e: Exception) {
            OrderResult.Failure(e.localizedMessage ?: "Unknown error")
        }
    }
    suspend fun getTransactionWithUserAndItems(transactionId : Long): TransactionDetails {
        return storeDatabase.getStoreDao().getTransactionItemsWithUser(transactionId)
    }
    suspend fun getTransactions(): List<Transaction> {
        return storeDatabase.getStoreDao().getTransactions()
    }
}