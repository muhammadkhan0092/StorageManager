package com.example.storemanager.vm

import androidx.lifecycle.ViewModel
import com.example.storemanager.data.Item
import com.example.storemanager.data.Transaction
import com.example.storemanager.data.TransactionItem
import com.example.storemanager.data.User
import com.example.storemanager.repository.UserRepository
import com.example.storemanager.utils.OrderResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewTransactionVm @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){
    suspend fun getItem(barcode:String): Item? {
        return try {
            userRepository.getItem(barcode)
        } catch (e : Exception){
            null
        }
    }

    suspend fun placeOrder(user : User, transaction: Transaction,transactionItem: List<TransactionItem>): OrderResult {
        return userRepository.placeOrder(user,transaction,transactionItem)
    }

    suspend fun getTransactions(): List<Transaction> {
        return userRepository.getTransactions()
    }
}
