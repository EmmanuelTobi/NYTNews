package com.cosmic.nytnews.repository

import android.util.Log
import com.cosmic.nytnews.BuildConfig
import com.cosmic.nytnews.api.APIInstanceSetup
import com.cosmic.nytnews.model.NYTimesNewsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class NewsRepository {
    val TAG = "NewsRepository"
    private val apiService = APIInstanceSetup.RetrofitInstance.apiService

    fun getPostsLast1Day(): Flow<NYTimesNewsModel> {

        return flow {

            val newsRes = apiService.getPostsLastOneDay(BuildConfig.NYTimesAPIKey)
            emit(newsRes)

        }.catch {

            Log.d(TAG, "getPostsLast1Day Error: ${it.message}")
            when (it) {
                is HttpException -> {

                }
                else -> {

                }
            }

        }.flowOn(Dispatchers.IO)

    }

    fun getPostsLast7Days(): Flow<NYTimesNewsModel> {

        return flow {
            val newsRes = apiService.getPostsLastSevenDays(BuildConfig.NYTimesAPIKey)
            emit(newsRes)
        }.catch {
            when (it) {
                is HttpException -> {

                }
                else -> {

                }
            }
        }.flowOn(Dispatchers.IO)

    }

    fun getPostsLast30Days(): Flow<NYTimesNewsModel> {

        return flow {
            val newsRes = apiService.getPostsLastThirtyDays(BuildConfig.NYTimesAPIKey)
            emit(newsRes)
        }.catch {
            when (it) {
                is HttpException -> {

                }
                else -> {

                }
            }
        }.flowOn(Dispatchers.IO)

    }

}