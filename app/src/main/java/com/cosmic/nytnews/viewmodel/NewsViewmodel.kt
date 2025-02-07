package com.cosmic.nytnews.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic.nytnews.model.NYTimesNewsModel
import com.cosmic.nytnews.repository.NewsRepository
import kotlinx.coroutines.launch

class NewsViewmodel : ViewModel() {
    val TAG = "NewsViewmodel"
    private val repository = NewsRepository()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        fetchNewsPostsLast1Day()
    }

    private val _posts = MutableLiveData<NYTimesNewsModel>()
    val getNewsPosts: LiveData<NYTimesNewsModel> = _posts

    private fun fetchNewsPostsLast1Day() {
        viewModelScope.launch {
            try {

                val response = repository.getPostsLast1Day()
                response.collect {
                    if(it.status == "OK") {
                        _posts.value = it
                        Log.d(TAG, "fetchNewsPostsLast1Day: ${it.status}")
                    } else {
                        _error.value = "Error: Unable to get news post at the moment"
                    }
                }

            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                _error.value = "Exception: ${e.message}"
            }
        }
    }

    private val _sevenDaysNewPosts = MutableLiveData<NYTimesNewsModel>()
    val get7daysNewsPosts: LiveData<NYTimesNewsModel> = _posts

    private fun fetchNewsPostsLast7Days() {
        viewModelScope.launch {
            try {

                val response = repository.getPostsLast7Days()
                response.collect {
                    if(it.status == "OK") {
                        _sevenDaysNewPosts.value = it
                    } else {
                        _error.value = "Error: Unable to get news post at the moment"
                    }
                }

            } catch (e: Exception) {
                _error.value = "Exception: ${e.message}"
            }
        }
    }

    private val _thirtyDaysNewPosts = MutableLiveData<NYTimesNewsModel>()
    val getThirtyDaysNewsPosts: LiveData<NYTimesNewsModel> = _posts

    private fun fetchNewsPostsLast30Days() {
        viewModelScope.launch {
            try {

                val response = repository.getPostsLast7Days()
                response.collect {
                    if(it.status == "OK") {
                        _thirtyDaysNewPosts.value = it
                    } else {
                        _error.value = "Error: Unable to get news post at the moment"
                    }
                }

            } catch (e: Exception) {
                _error.value = "Exception: ${e.message}"
            }
        }
    }

}