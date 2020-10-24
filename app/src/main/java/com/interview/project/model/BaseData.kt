package com.interview.project.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

data class BaseData(
    @SerializedName("data") var data: List<Data>? = null,
    @SerializedName("success") var success: Boolean = false,
    @SerializedName("status") var status: Int = 0
) : Serializable