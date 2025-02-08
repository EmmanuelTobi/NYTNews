package com.cosmic.nytnews.utils

sealed class ResultState<out T> {
    data class Success<out T>(val data : T) : ResultState<T>()
    data class Error(val message : String) : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()
}