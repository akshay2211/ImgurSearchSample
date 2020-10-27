package com.interview.project.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.interview.project.R
import com.interview.project.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class SettingsFragment : PreferenceFragmentCompat() {
    private val coroutineContext by inject<CoroutineContext>()
    private val db by inject<AppDatabase>()

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        var dialogPreference =
            preferenceScreen.findPreference<Preference>("dialog_clear_cache")

        dialogPreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            Log.e("hello", "prefs")
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.cache_title)
                setMessage(R.string.cache_summary).setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    CoroutineScope(coroutineContext).launch {
                        db.imagesDao().deleteTable()
                        db.commentsDao().deleteTable()
                    }
                }.setNegativeButton("Cancel", null).show()
            }
            true
        }

    }


}