package com.example.storemanager.vm

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.storemanager.data.Item
import com.example.storemanager.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DownloadBarcodesVm @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(){
    fun getDownloadingItem(): Flow<PagingData<Item>> {
        return userRepository.getDownloadingItems()
    }
}
