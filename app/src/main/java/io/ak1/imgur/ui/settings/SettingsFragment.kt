package io.ak1.imgur.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.ak1.imgur.R
import io.ak1.imgur.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

/**
 * uses the androidx.preference library
 * to show preference screen and store shared data
 */
class SettingsFragment : PreferenceFragmentCompat() {
    private val coroutineContext by inject<CoroutineContext>()
    private val db by inject<AppDatabase>()

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // preference is loaded from R.xml.preferences
        setPreferencesFromResource(R.xml.preferences, rootKey)

        var dialogPreference =
            preferenceScreen.findPreference<Preference>("dialog_clear_cache")

        dialogPreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.cache_title)
                setMessage(R.string.cache_summary).setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    try {
                        Thread {
                            // This method must be called on a background thread.
                            Glide.get(context).clearDiskCache()
                        }.start()
                    } catch (E: Exception) {
                    }
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