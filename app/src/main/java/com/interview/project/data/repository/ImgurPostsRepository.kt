package com.interview.project.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.toLiveData
import com.interview.project.data.local.AppDatabase
import com.interview.project.data.remote.ApiList
import com.interview.project.model.BaseData
import com.interview.project.model.Images
import com.interview.project.ui.utils.CustomBoundaryCallback
import com.interview.project.ui.utils.LiveDataCollection
import com.interview.project.ui.utils.NetworkState
import com.interview.project.ui.utils.extractMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext


/**
 * Created by akshay on 25,October,2020
 * akshay2211@github.io
 */
class ImgurPostsRepository(
    var context: Context,
    var db: AppDatabase,
    var apiList: ApiList,
    var coroutineContext: CoroutineContext
) {

    /**
     * Inserts the response into the database while also assigning position indices to items.
     */
    private fun insertResultIntoDb(search_content: String, body: BaseData?) {
        body!!.data.let { posts ->
            db.runInTransaction {
                var start = db.imagesDao().getNextIndexInSearch(search_content) + 1
                var page = db.imagesDao().getNextPageInSearch(search_content) + 1

                Log.e("check", "insertResultIntoDb  start $start      page $page")
                posts?.forEachIndexed { index, data ->
                    if (!data.images.isNullOrEmpty()) {
                        //  Log.e("check images ", "${data.images!![0].title}")
                        CoroutineScope(this.coroutineContext).launch {
                            db.imagesDao().insert(data.images!!.mapIndexed { index1, images ->

                                images.indexInResponse = start
                                start++
                                images.pageNumber = page
                                images.search_content = search_content
                                Log.e(
                                    "images ",
                                    "$page      --           ${images.link}  ${images.indexInResponse}"
                                )
                                images
                            })
                        }
                    }
                }
            }
        }
    }

    fun refresh(search_content: String): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING
        CoroutineScope(this.coroutineContext).launch {
            try {


                var response = apiList.getSearchList("1", search_content)
                Log.e("refresh response api", "->${response.code()}")
                if (!response.isSuccessful) {
                    var error = response.errorBody()
                    networkState.value = NetworkState.error(error?.extractMessage())
                } else {
                    db.imagesDao().deleteBySearchContents(search_content)
                    insertResultIntoDb(search_content, response.body())

                    // since we are in bg thread now, post the result.
                    networkState.postValue(NetworkState.LOADED)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return networkState
    }

    fun getImgurPosts(search_content: String = "", i: Int): LiveDataCollection<Images> {
        val boundaryCallback = CustomBoundaryCallback(
            context = context,
            apiList = apiList,
            searchedContent = search_content,
            coroutineContext = coroutineContext,
            handleResponse = this::insertResultIntoDb,
            ioExecutor = Executors.newSingleThreadExecutor(),
            networkPageSize = i
        )
        // we are using a mutable live data to trigger refresh requests which eventually calls
        // refresh method and gets a new live data. Each refresh request by the user becomes a newly
        // dispatched data in refreshTrigger
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = refreshTrigger.switchMap {
            refresh(search_content)
        }
        val livePagedList = db.imagesDao().postsBySearchContents(search_content).toLiveData(
            pageSize = i,
            boundaryCallback = boundaryCallback
        )

        return LiveDataCollection(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState,
            retry = {
                boundaryCallback.helper.retryAllFailed()
            },
            refresh = {
                refreshTrigger.value = null
            },
            refreshState = refreshState
        )
    }


}


