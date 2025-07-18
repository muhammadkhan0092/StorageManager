package com.example.storemanager.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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
            .cachedIn(viewModelScope) // ✅ Cache here
    }

    fun getSpecificUserTransaction(mobileNo: String): Flow<PagingData<TransactionWithUser>> {
        return userRepository.getSpecificUserWithTransaction(mobileNo)
            .cachedIn(viewModelScope) // ✅ Cache this too
    }
}
