package com.example.storemanager.repository

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

    suspend fun placeOrder(
        user: User,
        transaction: Transaction,
        transactionItems: List<TransactionItem>
    ): OrderResult {
        return try {
            storeDatabase.withTransaction {
                storeDatabase.getStoreDao().insertUser(user)
                val generatedId = storeDatabase.getStoreDao().insertTransaction(transaction)
                val updatedItems = transactionItems.map {
                    it.copy(transactionId = generatedId)
                }
                storeDatabase.getStoreDao().insertTransactionItem(updatedItems)
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