package com.example.storemanager.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storemanager.data.Item
import com.example.storemanager.repository.UserRepository
import com.example.storemanager.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AddItemVm @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _addItemFlow = MutableStateFlow<Resource<String>>(Resource.Unspecified())
    val addItemFlow: StateFlow<Resource<String>> = _addItemFlow.asStateFlow()



    fun insertItem(item: Item) {
        viewModelScope.launch {
            _addItemFlow.emit(Resource.Loading())
            try {
                userRepository.setItem(item)
                _addItemFlow.emit(Resource.Success("Item Inserted"))
            } catch (e: Exception) {
                _addItemFlow.emit(Resource.Error(e.message ?: "Unknown Error"))
            }
        }
    }


}
