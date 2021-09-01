package io.ak1.imgur.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import io.ak1.imgur.R
import io.ak1.imgur.ui.utils.isDarkThemeOn
import io.ak1.imgur.ui.utils.setUpStatusNavigationBarColors
import io.ak1.imgur.ui.utils.setupTheme
import kotlinx.android.synthetic.main.settings_activity.*


class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setUpStatusNavigationBarColors(
            isDarkThemeOn(),
            ContextCompat.getColor(this, R.color.background)
        )
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SettingsFragment.newInstance())
                .commitNow()
        }
        toolbar.setNavigationOnClickListener { finish() }
        sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)

    }

    override fun onResume() {
        super.onResume()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key?.equals("list_theme") == true) {
            sharedPreferences?.setupTheme(key, resources)
            window.setUpStatusNavigationBarColors(
                isDarkThemeOn(),
                ContextCompat.getColor(this, R.color.background)
            )
        }

    }

}

