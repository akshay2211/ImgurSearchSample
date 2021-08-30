package io.ak1.imgur.di

import android.content.Context
import androidx.room.Room
import io.ak1.imgur.BuildConfig
import io.ak1.imgur.data.local.AppDatabase
import io.ak1.imgur.data.remote.ApiList
import kotlinx.coroutines.Dispatchers
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.coroutines.CoroutineContext


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
    val client = OkHttpClient.Builder()
        .connectionSpecs(Arrays.asList(ConnectionSpec.COMPATIBLE_TLS))
        .addInterceptor(interceptor)
        .build()

    return Retrofit.Builder().baseUrl(ApiList.BASE_PATH)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client).build()
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

fun getCoroutineContext(): CoroutineContext {
    return Dispatchers.IO
}
