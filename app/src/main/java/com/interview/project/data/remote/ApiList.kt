package com.interview.project.data.remote

import com.interview.project.model.BaseData
import com.interview.project.ui.utils.Constants.IMGUR_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */
interface ApiList {

    //https://api.imgur.com/3/gallery/search/1?q=vanilla
    @Headers("Authorization:CLIENT-ID $IMGUR_API_KEY")
    @GET("gallery/search/{page}")
    suspend fun getSearchList(@Path("page") page: String, @Query("q") q: String): Response<BaseData>

    companion object {
        const val BASE_PATH = "https://api.imgur.com/3/"
    }
}
