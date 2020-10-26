package com.interview.project.ui.utils

import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
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
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            CoroutineScope(this.coroutineContext).launch {
                var response = apiList.getSearchList("1", searchedContent)
                if (!response.isSuccessful) {
                    var error = response.errorBody()
                    it.recordFailure(IllegalStateException(error?.extractMessage()))
                    return@launch
                }
                insertItemsIntoDb(response, it)

            }
        }
    }


    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Images) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            CoroutineScope(this.coroutineContext).launch {
                var response =
                    apiList.getSearchList(itemAtEnd.pageNumber.toString(), searchedContent)
                if (!response.isSuccessful) {
                    var error = response.errorBody()
                    it.recordFailure(IllegalStateException(error?.extractMessage()))
                    return@launch
                }
                insertItemsIntoDb(response, it)
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