package com.interview.project.ui.utils

import android.content.Context
import androidx.annotation.MainThread
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import com.interview.project.R
import com.interview.project.data.remote.ApiList
import com.interview.project.model.BaseData
import com.interview.project.model.Images
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.UnknownHostException
import java.util.concurrent.Executor
import kotlin.coroutines.CoroutineContext

/**
 * Created by akshay on 25,October,2020
 * akshay2211@github.io
 */

/**
 * api calls are triggered from [CustomBoundaryCallback] and the paged lists data is managed by
 * the paging library
 */
class CustomBoundaryCallback(
    private val context: Context,
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
            var response: Response<BaseData>?
            CoroutineScope(this.coroutineContext).launch {
                try {
                    response = apiList.getSearchList("1", searchedContent)
                    if (response?.isSuccessful != true) {
                        val error = response?.errorBody()
                        it.recordFailure(IllegalStateException(error?.extractMessage()))
                        return@launch
                    }
                    insertItemsIntoDb(response!!, it)
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                    it.recordFailure(IllegalStateException(context.resources.getString(R.string.internet_error)))
                } catch (e: Exception) {
                    e.printStackTrace()
                    it.recordFailure(IllegalStateException(e))
                }
            }
        }
    }


    @MainThread
    override fun onItemAtEndLoaded(itemAtEnd: Images) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            var response: Response<BaseData>?
            CoroutineScope(this.coroutineContext).launch {
                try {
                    response =
                        apiList.getSearchList(
                            (itemAtEnd.pageNumber + 1).toString(),
                            searchedContent
                        )
                    if (response?.body() == null) {
                        val error = response?.errorBody()
                        it.recordFailure(IllegalStateException(error?.extractMessage()))
                        return@launch
                    }
                    insertItemsIntoDb(response!!, it)
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                    it.recordFailure(IllegalStateException(context.resources.getString(R.string.internet_error)))
                } catch (e: Exception) {
                    e.printStackTrace()
                    it.recordFailure(IllegalStateException(e))
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