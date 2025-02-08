package com.cosmic.nytnews.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmic.nytnews.model.NYTimesNewsModel
import com.cosmic.nytnews.repository.NewsRepository
import com.cosmic.nytnews.utils.ResultState
import kotlinx.coroutines.launch

class NewsViewmodel : ViewModel() {
    val TAG = "NewsViewmodel"
    private val repository = NewsRepository()

    private val _posts = MutableLiveData<ResultState<NYTimesNewsModel>>(ResultState.Loading)
    val getNewsPosts: MutableLiveData<ResultState<NYTimesNewsModel>> = _posts

    init {
        fetchNewsPostsLast1Day()
    }

    fun fetchNewsPostsLast1Day() {
        _posts.value = ResultState.Loading
        viewModelScope.launch {
            try {
                val response = repository.getPostsLast1Day()
                response.collect {
                    if(it.status == "OK") {
                        _posts.value = ResultState.Success(it)
                    } else {
                        _posts.value = ResultState.Error("Error: Unable to get news post at the moment")
                    }
                }

            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                _posts.value = ResultState.Error(e.message.toString())
            }
        }
    }

    fun fetchNewsPostsLast7Days() {
        _posts.value = ResultState.Loading
        viewModelScope.launch {
            try {

                val response = repository.getPostsLast7Days()
                response.collect {
                    if(it.status == "OK") {
                        _posts.value = ResultState.Success(it)
                    } else {
                        _posts.value = ResultState.Error("Error: Unable to get news post at the moment")
                    }
                }

            } catch (e: Exception) {
                _posts.value = ResultState.Error(e.message.toString())
            }
        }
    }

    fun fetchNewsPostsLast30Days() {
        _posts.value = ResultState.Loading
        viewModelScope.launch {
            try {

                val response = repository.getPostsLast7Days()
                response.collect {
                    if(it.status == "OK") {
                        _posts.value = ResultState.Success(it)
                    } else {
                        _posts.value = ResultState.Error("Error: Unable to get news post at the moment")
                    }
                }

            } catch (e: Exception) {
                _posts.value = ResultState.Error(e.message.toString())
            }
        }
    }

}