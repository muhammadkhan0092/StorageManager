package com.example.storemanager.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.storemanager.data.Item
import com.example.storemanager.data.TransactionDetails
import com.example.storemanager.data.TransactionWithUser
import com.example.storemanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SearchTransactionVm @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){
    fun getUserWithTransaction(): Flow<PagingData<TransactionWithUser>> {
        return userRepository.getUserWithTransacation()
    }
}
