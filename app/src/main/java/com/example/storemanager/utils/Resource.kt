package com.example.storemanager.utils

sealed class Resource<T> (
    val data : T? = null,
    val message : String? = null
){
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String) : Resource<T>(message = message)
    class Loading<T> : Resource<T>()
    class Unspecified<T> : Resource<T>()
}