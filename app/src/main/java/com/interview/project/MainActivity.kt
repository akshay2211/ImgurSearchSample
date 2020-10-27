package com.interview.project

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.interview.project.ui.adapters.ImagesListAdapter
import com.interview.project.ui.main.MainActivityViewModel
import com.interview.project.ui.singlepost.SingleActivity
import com.interview.project.ui.utils.GlideApp
import com.interview.project.ui.utils.debounce
import com.interview.project.ui.utils.getScreenSize
import com.interview.project.ui.utils.setUpStatusNavigationBarColors
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_header.*
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MainActivity : AppCompatActivity() {
    val liveViewModel: MainActivityViewModel by stateViewModel()
    var ctx = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setUpStatusNavigationBarColors()
        resources.displayMetrics.getScreenSize()
        setContentView(R.layout.activity_main)
        initAdapter()

        // initSwipeToRefresh()
        initSearch()
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = ImagesListAdapter(glide, {
            liveViewModel.retry()
        }) {
            SingleActivity.startActivity(this, it)
        }
        var manager = GridLayoutManager(
            this, 4,
            GridLayoutManager.VERTICAL, false
        ).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.getItemViewType(position)) {
                        R.layout.images_row -> 1
                        R.layout.network_state_item -> 4
                        else -> 1
                    }
                }
            }
        }
        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter
        liveViewModel.posts.observe(this, {
            adapter.submitList(it) {
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
            adapter.setNetworkState(it)
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
}