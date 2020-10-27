package com.interview.project.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.interview.project.data.repository.ImgurPostsRepository

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */
class MainActivityViewModel(
    val handle: SavedStateHandle,
    private val repository: ImgurPostsRepository
) :
    ViewModel() {
    companion object {
        const val KEY_IMGUR = "imgur_search_key"
        const val DEFAULT_IMGUR_SEARCH = "vanilla"
    }

    init {
        if (!handle.contains(KEY_IMGUR)) {
            handle.set(KEY_IMGUR, DEFAULT_IMGUR_SEARCH)
        }
    }

    private val repoResult = handle.getLiveData<String>(KEY_IMGUR).map {
        repository.getImgurPosts(it, 30)
    }
    val posts = repoResult.switchMap { it.pagedList }
    val networkState = repoResult.switchMap { it.networkState }
    val refreshState = repoResult.switchMap { it.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun showSearchedContent(search_content: String): Boolean {
        if (handle.get<String>(KEY_IMGUR) == search_content) {
            return false
        }
        handle.set(KEY_IMGUR, search_content)
        return true
    }

    fun retry() {
        val listing = repoResult.value
        listing?.retry?.invoke()
    }

}