package com.interview.project.ui.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import com.google.gson.Gson
import com.interview.project.data.remote.ApiList
import com.interview.project.model.BaseData
import com.interview.project.model.Images
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

/**
 * Created by akshay on 25,October,2020
 * akshay2211@github.io
 */
class CustomBoundaryCallback(
    private val searchedContent: String,
    private val apiList: ApiList,
    private val handleResponse: (String, BaseData?) -> Unit,
    private val coroutineContext: CoroutineContext,
    private val ioExecutor: Executor,
    private val networkPageSize: Int
) : PagedList.BoundaryCallback<Images>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()


    @MainThread
    override fun onZeroItemsLoaded() {
        Log.e("onZeroItemsLoaded", "onZeroItemsLoaded")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            var response: Response<BaseData>? = null
            CoroutineScope(this.coroutineContext).launch {
                try {
                    response = apiList.getSearchList("1", searchedContent)
                    Log.e("response api", "onZeroItemsLoaded ->${response?.code()}")
                    Log.e(
                        "response api",
                        "onZeroItemsLoaded ->${Gson().toJson(response?.body())} ${
                            Gson().toJson(response?.errorBody())
                        }"
                    )

                    if (response?.isSuccessful != true) {
                        var error = response?.errorBody()
                        it.recordFailure(IllegalStateException(error?.extractMessage()))
                        return@launch
                    }
                    insertItemsIntoDb(response!!, it)
                } catch (e: Exception) {
                    e.printStackTrace()
                    it.recordFailure(IllegalStateException("error"))
                    Log.e(
                        "response api",
                        "onZeroItemsLoaded Exception ->${response?.message()}   ${response?.code()}"
                    )
                }
            }
        }
    }


    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Images) {
        Log.e("onItemAtEndLoaded", "onItemAtEndLoaded ${itemAtEnd.pageNumber + 1}")
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            Log.e("onItemAtEndLoaded", "actual call onItemAtEndLoaded ${itemAtEnd.pageNumber + 1}")
            var response: Response<BaseData>? = null
            CoroutineScope(this.coroutineContext).launch {
                try {
                    response =
                        apiList.getSearchList(
                            (itemAtEnd.pageNumber + 1).toString(),
                            searchedContent
                        )
                    Log.e("response api", "onItemAtEndLoaded ->${response?.code()}")
                    Log.e(
                        "response api",
                        "onItemAtEndLoaded ->${Gson().toJson(response?.body())} ${
                            Gson().toJson(response?.errorBody())
                        }"
                    )
                    if (response?.body() == null) {
                        var error = response?.errorBody()
                        it.recordFailure(IllegalStateException(error?.extractMessage()))
                        return@launch
                    }
                    insertItemsIntoDb(response!!, it)
                } catch (e: Exception) {
                    e.printStackTrace()
                    it.recordFailure(IllegalStateException("error"))
                    Log.e("response api", "onItemAtEndLoaded Exception ->${response?.code()}")
                }
            }

        }
    }

    private fun insertItemsIntoDb(
        response: Response<BaseData>,
        it: PagingRequestHelper.Request.Callback
    ) {
        ioExecutor.execute {
            handleResponse(searchedContent, response.body())
            it.recordSuccess()
        }
    }
}