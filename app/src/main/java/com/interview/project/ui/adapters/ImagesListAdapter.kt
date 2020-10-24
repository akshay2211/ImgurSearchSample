package com.interview.project.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.interview.project.R
import com.interview.project.model.Images

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

class ImagesListAdapter :
    PagedListAdapter<Images, ImagesListAdapter.ImagesViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Images>() {
            override fun areItemsTheSame(oldItem: Images, newItem: Images) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Images, newItem: Images) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder =
        ImagesViewHolder(parent)

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    class ImagesViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.images_row, parent, false
        )
    ) {
        fun bindTo(notes: Images?) {
            //   itemView.imageView.text = (notes?.text ?: "Loading...   ") + "        " + notes?.uid
        }
    }

}