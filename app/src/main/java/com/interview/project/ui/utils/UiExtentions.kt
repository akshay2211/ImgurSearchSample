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