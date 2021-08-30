package io.ak1.imgur.ui.utils

/**
 * Created by akshay on 25,October,2020
 * akshay2211@github.io
 */

enum class State {
    RUNNING,
    SUCCESS,
    FAILED
}

data class NetworkState private constructor(
    val State: State,
    val msg: String? = null
) {
    companion object {
        val LOADED = NetworkState(State.SUCCESS)
        val LOADING = NetworkState(State.RUNNING)
        fun error(msg: String?) = NetworkState(State.FAILED, msg)
    }
}