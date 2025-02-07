package com.cosmic.nytnews.api

import com.cosmic.nytnews.model.NYTimesNewsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface APIServices {

    @GET("svc/mostpopular/v2/viewed/1.json")
    suspend fun getPostsLastOneDay(@Query("api-key") apiKey: String): NYTimesNewsModel

    @GET("svc/mostpopular/v2/viewed/7.json")
    suspend fun getPostsLastSevenDays(@Query("api-key") apiKey: String): NYTimesNewsModel

    @GET("svc/mostpopular/v2/viewed/30.json")
    suspend fun getPostsLastThirtyDays(@Query("api-key") apiKey: String): NYTimesNewsModel

}