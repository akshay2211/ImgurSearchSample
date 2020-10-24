package com.interview.project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.interview.project.ui.adapters.ImagesListAdapter
import com.interview.project.ui.main.MainActivityViewHolder
import com.interview.project.ui.utils.setUpStatusNavigationBarColors
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val liveViewModel by inject<MainActivityViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setUpStatusNavigationBarColors()
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        recyclerView.apply { adapter = ImagesListAdapter() }
        liveViewModel.imagesList.observe(this, Observer {
            (recyclerView.adapter as ImagesListAdapter).submitList(it)
        })
    }
}