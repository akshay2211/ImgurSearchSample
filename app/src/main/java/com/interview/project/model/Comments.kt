package com.interview.project.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by akshay on 27,October,2020
 * akshay2211@github.io
 */
@Entity(tableName = "comments_table")
data class Comments(
    @SerializedName("post_id") var post_id: String,
    @SerializedName("comment_content") var comment_content: String
) : Serializable {
    @PrimaryKey
    @SerializedName("id")
    var id: String = UUID.randomUUID().toString()
}