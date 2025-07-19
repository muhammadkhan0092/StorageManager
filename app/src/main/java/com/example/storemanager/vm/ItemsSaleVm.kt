package com.example.storemanager.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storemanager.data.ItemSalesSummary
import com.example.storemanager.data.TransactionWithUser
import com.example.storemanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ItemsSaleVm @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){
    suspend fun getUserWithTransaction(): List<ItemSalesSummary>{
        return userRepository.getItemsSaleSummary()
    }


}
