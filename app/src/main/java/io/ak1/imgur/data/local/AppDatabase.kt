package io.ak1.imgur.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import io.ak1.imgur.model.Comments
import io.ak1.imgur.model.Images

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

@Database(entities = [Images::class, Comments::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImagesDao
    abstract fun commentsDao(): CommentsDao

}

@Dao
interface ImagesDao {
    @Query("SELECT * FROM images_table WHERE search_content = :search_content ORDER BY indexInResponse ASC")
    fun postsBySearchContents(search_content: String): DataSource.Factory<Int, Images>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(images: List<Images>)

    @Query("SELECT MAX(indexInResponse) FROM images_table WHERE search_content = :search_content")
    fun getNextIndexInSearch(search_content: String): Int


    @Query("SELECT MAX(pageNumber) FROM images_table WHERE search_content = :search_content")
    fun getNextPageInSearch(search_content: String): Int

    @Query("DELETE FROM images_table WHERE search_content = :search_content")
    fun deleteBySearchContents(search_content: String)

    @Query("DELETE FROM images_table")
    suspend fun deleteTable()

    @Query("SELECT COUNT(id) FROM images_table WHERE search_content = :search_content")
    fun getCount(search_content: String): Int
}

@Dao
interface CommentsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comments: Comments)

    @Query("SELECT * FROM comments_table WHERE post_id = :post_id")
    fun getAllComments(post_id: String): LiveData<List<Comments>>

    @Query("DELETE FROM comments_table")
    suspend fun deleteTable()
}
