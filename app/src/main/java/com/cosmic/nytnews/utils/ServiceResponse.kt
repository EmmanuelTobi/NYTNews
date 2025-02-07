package com.cosmic.nytnews.utils

sealed class ServiceResponse<out T> {
    data class Success<out T>(val data : T) : ServiceResponse<T>()
    data class Error(val message : String) : ServiceResponse<Nothing>()
    data object Loading : ServiceResponse<Nothing>()
}