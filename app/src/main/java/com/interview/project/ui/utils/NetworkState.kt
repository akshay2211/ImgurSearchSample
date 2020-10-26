package com.interview.project.ui.utils

/**
 * Created by akshay on 25,October,2020
 * akshay2211@github.io
 */

enum class State1 {
    RUNNING,
    SUCCESS,
    FAILED
}

data class NetworkState private constructor(
    val State: State1,
    val msg: String? = null
) {
    companion object {
        val LOADED = NetworkState(State1.SUCCESS)
        val LOADING = NetworkState(State1.RUNNING)
        fun error(msg: String?) = NetworkState(State1.FAILED, msg)
    }
}