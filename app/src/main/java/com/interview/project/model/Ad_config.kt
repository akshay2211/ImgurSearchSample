package com.interview.project.model

import com.google.gson.annotations.SerializedName

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

data class Ad_config(

	@SerializedName("safeFlags") val safeFlags: List<String>,
	@SerializedName("highRiskFlags") val highRiskFlags: List<String>,
	@SerializedName("unsafeFlags") val unsafeFlags: List<String>,
	@SerializedName("wallUnsafeFlags") val wallUnsafeFlags: List<String>,
	@SerializedName("showsAds") val showsAds: Boolean
)