package io.ak1.imgur.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

data class Data(
    @SerializedName("id") var id: String = "",
    @SerializedName("title") var title: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("datetime") var datetime: Int = 0,
    @SerializedName("images") var images: List<Images>? = null

) : Serializable

/*@SerializedName("id") val id : String,
	@SerializedName("title") val title : String,
	@SerializedName("description") val description : String,
	@SerializedName("datetime") val datetime : Int,
	@SerializedName("cover") val cover : String,
	@SerializedName("cover_width") val cover_width : Int,
	@SerializedName("cover_height") val cover_height : Int,
	@SerializedName("account_url") val account_url : String,
	@SerializedName("account_id") val account_id : Int,
	@SerializedName("privacy") val privacy : String,
	@SerializedName("layout") val layout : String,
	@SerializedName("views") val views : Int,
	@SerializedName("link") val link : String,
	@SerializedName("ups") val ups : Int,
	@SerializedName("downs") val downs : Int,
	@SerializedName("points") val points : Int,
	@SerializedName("score") val score : Int,
	@SerializedName("is_album") val is_album : Boolean,
	@SerializedName("vote") val vote : String,
	@SerializedName("favorite") val favorite : Boolean,
	@SerializedName("nsfw") val nsfw : Boolean,
	@SerializedName("section") val section : String,
	@SerializedName("comment_count") val comment_count : Int,
	@SerializedName("favorite_count") val favorite_count : Int,
	@SerializedName("topic") val topic : String,
	@SerializedName("topic_id") val topic_id : Int,
	@SerializedName("images_count") val images_count : Int,
	@SerializedName("in_gallery") val in_gallery : Boolean,
	@SerializedName("is_ad") val is_ad : Boolean,
	@SerializedName("tags") val tags : List<Tags>,
	@SerializedName("ad_type") val ad_type : Int,
	@SerializedName("ad_url") val ad_url : String,
	@SerializedName("in_most_viral") val in_most_viral : Boolean,
	@SerializedName("include_album_ads") val include_album_ads : Boolean,
	@SerializedName("images") val images : List<Images>,
	@SerializedName("ad_config") val ad_config : Ad_config*/