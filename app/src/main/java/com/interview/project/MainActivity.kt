package com.interview.project

import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.interview.project.ui.adapters.ImagesListAdapter
import com.interview.project.ui.main.MainActivityViewModel
import com.interview.project.ui.singlepost.SingleActivity
import com.interview.project.ui.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_header.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MainActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {
    private val liveViewModel: MainActivityViewModel by stateViewModel()
    private lateinit var imagesAdapter: ImagesListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setUpStatusNavigationBarColors(
            isDarkThemeOn(),
            ContextCompat.getColor(this, R.color.background)
        )
        resources.displayMetrics.getScreenSize()
        setContentView(R.layout.activity_main)
        initAdapter()
        initSwipeToRefresh()
        initSearch()
        initSettings()
    }



    private fun initAdapter() {
        val glide = GlideApp.with(this)
        imagesAdapter = ImagesListAdapter(glide, {
            liveViewModel.retry()
        }) {
            SingleActivity.startActivity(this, it)
        }
        var manager = getGridLayoutManager(ORIENTATION_PORTRAIT, imagesAdapter)
        recyclerView.apply {
            layoutManager = manager
            adapter = imagesAdapter
        }
        liveViewModel.posts.observe(this, {
            imagesAdapter.submitList(it) {
                // Workaround for an issue where RecyclerView incorrectly uses the loading / spinner
                // item added to the end of the list as an anchor during initial load.
                val layoutManager = manager
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    recyclerView.scrollToPosition(position)
                }
            }
        })
        liveViewModel.networkState.observe(this, {
            Log.e("networkState", "${it.State.name}")
            imagesAdapter.setNetworkState(it)
        })
    }


    private fun initSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText.debounce { string ->
                    updatedSubredditFromInput()
                }
                return false
            }
        })
        searchView.setQuery("vanilla", true)
    }

    private fun updatedSubredditFromInput() {
        searchView.query.trim().toString().let {
            if (it.isNotEmpty()) {
                if (liveViewModel.showSearchedContent(it)) {
                    recyclerView.scrollToPosition(0)
                    (recyclerView.adapter as? ImagesListAdapter)?.submitList(null)
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        var manager = getGridLayoutManager(newConfig.orientation, imagesAdapter)
        recyclerView.layoutManager = manager
    }

    private fun initSwipeToRefresh() {
        liveViewModel.refreshState.observe(this, {
            swipe_refresh.isRefreshing = it == NetworkState.LOADING
        })
        swipe_refresh.setOnRefreshListener {
            liveViewModel.refresh()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        //The Refresh must be only active when the offset is zero :
        swipe_refresh.isEnabled = verticalOffset == 0
    }

    override fun onResume() {
        super.onResume()
        appBarLayout.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        appBarLayout.removeOnOffsetChangedListener(this)
    }

    private fun initSettings() {
        settings.setOnClickListener {
            startSettingsActivity()
        }
    }

}