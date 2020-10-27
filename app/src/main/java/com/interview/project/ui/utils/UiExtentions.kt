@file:JvmName("CustomWindow")

package com.interview.project.ui.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.GridLayoutManager
import com.interview.project.R
import com.interview.project.ui.adapters.ImagesListAdapter


/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */


fun Window.setUpStatusNavigationBarColors(isLight: Boolean = false, colorCode: Int = Color.WHITE) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        statusBarColor = colorCode
        navigationBarColor = colorCode
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        setDecorFitsSystemWindows(isLight)
    } else {
        @Suppress("DEPRECATION")
        decorView.systemUiVisibility = if (isLight) {
            0
        } else {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}

fun String?.debounce(callback: (String) -> Unit) {
    if (debounceRunnable != null) {
        debounceHandler.removeCallbacks(debounceRunnable!!)
    }
    debounceRunnable = Runnable {
        callback(this.toString())
    }
    debounceHandler.postDelayed(debounceRunnable!!, 350)
}

var debounceRunnable: Runnable? = null
var debounceHandler: Handler = Handler(Looper.getMainLooper())


fun AppCompatEditText.hideKeyboard(context: Context) {
    val manager: InputMethodManager? =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    manager?.hideSoftInputFromWindow(
        this.windowToken, 0
    )
}

fun Context.getGridLayoutManager(
    orientationPortrait: Int,
    adapter: ImagesListAdapter
): GridLayoutManager {

    var spancount = when (orientationPortrait) {
        Configuration.ORIENTATION_PORTRAIT -> 3
        Configuration.ORIENTATION_LANDSCAPE -> 4
        else -> 3
    }
    return GridLayoutManager(
        this, spancount,
        GridLayoutManager.VERTICAL, false
    ).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    R.layout.images_row -> 1
                    R.layout.network_state_item -> spancount
                    else -> 1
                }
            }
        }
    }
}