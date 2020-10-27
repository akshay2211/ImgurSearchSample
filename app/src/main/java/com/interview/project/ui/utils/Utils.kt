package com.interview.project.ui.utils

import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * Created by akshay on 25,October,2020
 * akshay2211@github.io
 */

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


private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

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

fun DisplayMetrics.getScreenSize() {
    Constants.HeightPX = heightPixels
    Constants.WidthPX = widthPixels
    Constants.DENSITY = density
    Log.e(
        "WidthPX HeightPX",
        "" + Constants.WidthPX.toString() + " " + Constants.HeightPX
    )
}

fun Int.toPx(): Int {
    return (this * Constants.DENSITY).toInt()
}

fun Float.toPx(): Float {
    return (this * Constants.DENSITY)
}

fun Int.toDp(): Int {
    return (this / Constants.DENSITY).toInt()
}

fun Float.toDp(): Float {
    return (this / Constants.DENSITY)
}

