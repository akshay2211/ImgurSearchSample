package com.interview.project.ui.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.signature.ObjectKey
import com.interview.project.R
import com.interview.project.model.Images
import com.interview.project.ui.utils.GlideRequests
import com.interview.project.ui.utils.NetworkState
import com.interview.project.ui.utils.State
import kotlinx.android.synthetic.main.images_row.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

/**
 * [ImagesListAdapter] is a [PagedListAdapter] with two view holders
 * [ImagesViewHolder] for images and [NetworkStateItemViewHolder] for showing error and loading states
 */
class ImagesListAdapter(
    var glideRequests: GlideRequests,
    private val retryCallback: () -> Unit,
    private val clickCallback: (images: Images) -> Unit
) :
    PagedListAdapter<Images, RecyclerView.ViewHolder>(POST_COMPARATOR) {
    private var networkState: NetworkState? = null
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.images_row -> (holder as ImagesViewHolder).bindTo(getItem(position))
            R.layout.network_state_item -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.images_row -> ImagesViewHolder(parent, glideRequests, clickCallback)
            R.layout.network_state_item -> NetworkStateItemViewHolder(parent, retryCallback)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.images_row
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Images>() {
            override fun areContentsTheSame(oldItem: Images, newItem: Images): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Images, newItem: Images): Boolean =
                oldItem.id == newItem.id

        }
    }
}


class ImagesViewHolder(
    parent: ViewGroup,
    var glideRequests: GlideRequests,
    private val clickCallback: (image: Images) -> Unit
) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.images_row, parent, false
        )
    ) {
    private var images: Images? = null

    init {
        itemView.imageView.setOnClickListener {
            images?.let {
                clickCallback(it)
            }
        }
    }

    fun bindTo(images: Images?) {
        this.images = images
        glideRequests.load(images?.link)
            .placeholder(ColorDrawable(Color.GRAY))
            .error(ColorDrawable(Color.GRAY))
            .thumbnail(
                glideRequests.load(images?.link).override(300)
                    .transform(CenterCrop())
            )
            .transform(CenterCrop())
            .signature(ObjectKey(images?.id ?: ""))
            .transition(withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(itemView.imageView)
    }
}

class NetworkStateItemViewHolder(
    parent: ViewGroup,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.network_state_item, parent, false)
) {
    init {
        itemView.retry_button.setOnClickListener {
            retryCallback()
        }
    }

    fun bindTo(networkState: NetworkState?) {
        itemView.progress_bar.visibility = toVisibility(networkState?.State == State.RUNNING)
        itemView.retry_button.visibility = toVisibility(networkState?.State == State.FAILED)
        itemView.error_msg.visibility = toVisibility(networkState?.msg != null)
        itemView.error_msg.text = networkState?.msg
    }

    companion object {
        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}


