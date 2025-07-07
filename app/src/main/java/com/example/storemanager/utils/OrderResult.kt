package com.example.storemanager.utils

sealed class OrderResult {
    object Success : OrderResult()
    data class Failure(val message: String) : OrderResult()
}
