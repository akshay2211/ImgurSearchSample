package com.interview.project.di

import android.content.Context
import androidx.room.Room
import com.interview.project.BuildConfig
import com.interview.project.data.local.AppDatabase
import com.interview.project.data.remote.ApiList
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */


fun getLogInterceptor(): HttpLoggingInterceptor {

    return HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }

}

fun returnRetrofit(interceptor: HttpLoggingInterceptor): Retrofit {
    return Retrofit.Builder().baseUrl(ApiList.BASE_PATH)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder().addInterceptor(interceptor)
                .build()
        ).build()
}

fun getApi(retrofit: Retrofit): ApiList {
    return retrofit.create(ApiList::class.java)
}


fun getDb(context: Context): AppDatabase {
    return synchronized(context) {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-images"
        ).build()
    }
}
