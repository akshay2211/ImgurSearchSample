@file:JvmName("CustomWindow")

package com.interview.project.ui.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.interview.project.R
import com.interview.project.ui.adapters.ImagesListAdapter
import com.interview.project.ui.settings.SettingsActivity


/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

/**
 * extension [setUpStatusNavigationBarColors] to setup color codes
 * and themes according to themes
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                0
            }
        }
    }
}

/**
 * extension [isDarkThemeOn] checks the saved theme from preference
 * and returns boolean
 */
fun Context.isDarkThemeOn(): Boolean {
    var key = PreferenceManager.getDefaultSharedPreferences(this).getString("list_theme", "1")
    return when (key) {
        "2" -> true
        "1" -> false
        else -> return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }

}

/**
 * extension [debounce] generates the debounce
 * effect cancels the callbacks with in 250 milliseconds
 */
fun String?.debounce(callback: () -> Unit) {
    if (debounceRunnable != null) {
        debounceHandler.removeCallbacks(debounceRunnable!!)
    }
    debounceRunnable = Runnable {
        callback()
    }
    debounceHandler.postDelayed(debounceRunnable!!, 250)
}

var debounceRunnable: Runnable? = null
var debounceHandler: Handler = Handler(Looper.getMainLooper())

/**
 * extension [hideKeyboard] hides keyboard
 */
fun AppCompatEditText.hideKeyboard(context: Context) {
    val manager: InputMethodManager? =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    manager?.hideSoftInputFromWindow(
        this.windowToken, 0
    )
}

/**
 * extension [getGridLayoutManager] consists code for dynamic [GridLayoutManager]
 * to provide different spanCount for different views
 */
fun Context.getGridLayoutManager(
    orientationPortrait: Int,
    adapter: ImagesListAdapter
): GridLayoutManager {

    var spanCount = when (orientationPortrait) {
        Configuration.ORIENTATION_PORTRAIT -> 3
        Configuration.ORIENTATION_LANDSCAPE -> 4
        else -> 3
    }
    return GridLayoutManager(
        this, spanCount,
        GridLayoutManager.VERTICAL, false
    ).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    R.layout.images_row -> 1
                    R.layout.network_state_item -> spanCount
                    else -> 1
                }
            }
        }
    }
}

/**
 * extension [startSettingsActivity] starts [SettingsActivity]
 */
fun Context.startSettingsActivity() {
    startActivity(Intent(this, SettingsActivity::class.java))
}

/**
 * extension [setupTheme] calls the [AppCompatDelegate] methods which
 * setup the theme whenever the configuration is changed
 */
fun SharedPreferences?.setupTheme(key: String?, resources: Resources) {
    var value = this?.getString(key, "")
    val def = if (resources.configuration.uiMode and
        Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    ) {
        AppCompatDelegate.MODE_NIGHT_YES

    } else {
        AppCompatDelegate.MODE_NIGHT_NO
    }

    when (value) {
        "2" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        "1" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else -> AppCompatDelegate.setDefaultNightMode(def)
    }
}
