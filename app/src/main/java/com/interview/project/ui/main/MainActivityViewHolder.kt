package com.interview.project.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList.BoundaryCallback
import com.interview.project.data.local.AppDatabase
import com.interview.project.data.remote.ApiList
import com.interview.project.model.Images
import kotlinx.coroutines.launch

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */
class MainActivityViewHolder(private var context: Context, appdb: AppDatabase, apiList: ApiList) :
    ViewModel() {
    private val myImagesDataSource: DataSource.Factory<Int, Images> =
        appdb.imagesDao().imagesByDate()

    val imagesList = LivePagedListBuilder(myImagesDataSource, 5)
        .setBoundaryCallback(object :
            BoundaryCallback<Images>() {
            override fun onItemAtEndLoaded(itemAtEnd: Images) {
                super.onItemAtEndLoaded(itemAtEnd)
                Log.e("onItemAtEndLoaded", "-> ${itemAtEnd.id}  ${itemAtEnd.title}")
            }

            override fun onItemAtFrontLoaded(itemAtFront: Images) {
                super.onItemAtFrontLoaded(itemAtFront)
                Log.e("onItemAtFrontLoaded", "-> ${itemAtFront.id}  ${itemAtFront.title}")
            }

            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
                Log.e("onZeroItemsLoaded", "-> onZeroItemsLoaded")
                viewModelScope.launch {
                    var list = apiList.getSearchList("1", "vanilla")
                        .body()?.data
                    Log.e("check list count ", "${list?.size}")

                    list?.forEachIndexed { index, data ->
                        if (!data.images.isNullOrEmpty()) {
                            Log.e("check images ", "${data.images!![0].title}")
                            appdb.imagesDao().insert(data.images!!)
                        }
                    }

                }
            }

        }).build()
}