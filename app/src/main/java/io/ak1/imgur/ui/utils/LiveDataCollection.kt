package io.ak1.imgur.ui.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

/**
 * Created by akshay on 25,October,2020
 * akshay2211@github.io
 */
data class LiveDataCollection<T : Any>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val networkState: LiveData<NetworkState>,
    // represents the refresh status to show to the user. Separate from networkState, this
    // value is importantly only when refresh is requested.
    val refreshState: LiveData<NetworkState>,
    // refreshes the whole data and fetches it from scratch.
    val refresh: () -> Unit,
    // retries any failed requests.
    val retry: () -> Unit
)