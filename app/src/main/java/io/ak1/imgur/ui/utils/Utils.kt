package io.ak1.imgur.ui.utils

import android.util.DisplayMetrics
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * Created by akshay on 25,October,2020
 * akshay2211@github.io
 */

//extracts the error message from the errorbody from response
fun ResponseBody?.extractMessage(): String? {
    if (this == null) {
        return ""
    }
    return try {
        JSONObject(string()).getJSONObject("error").getString("message")
    } catch (e: Exception) {
        "Something went wrong"
    }
}

//extracts the error message emited from [PagingRequestHelper]
private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

//default NetworkState data passes in network status instance
fun PagingRequestHelper.createStatusLiveData(): LiveData<NetworkState> {
    val liveData = MutableLiveData<NetworkState>()
    addListener { report ->
        when {
            report.hasRunning() -> liveData.postValue(NetworkState.LOADING)
            report.hasError() -> liveData.postValue(
                NetworkState.error(getErrorMessage(report))
            )
            else -> liveData.postValue(NetworkState.LOADED)
        }
    }
    return liveData
}

//retrieve screen density and size in pixels
fun DisplayMetrics.getScreenSize() {
    Constants.HeightPX = heightPixels
    Constants.WidthPX = widthPixels
    Constants.DENSITY = density
}