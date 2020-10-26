package com.interview.project.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.interview.project.model.Images

/**
 * Created by akshay on 24,October,2020
 * akshay2211@github.io
 */

@Database(entities = [Images::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImagesDao

}

@Dao
interface ImagesDao {


    @Query("SELECT * FROM images_table WHERE search_content = :search_content ORDER BY indexInResponse ASC")
    fun postsBySearchContents(search_content: String): LiveData<List<Images>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(images: List<Images>)

    @Query("SELECT MAX(indexInResponse) + 1 FROM images_table WHERE search_content = :search_content")
    fun getNextIndexInSearch(search_content: String): Int


    @Query("SELECT MAX(pageNumber) + 1 FROM images_table WHERE search_content = :search_content")
    fun getNextPageInSearch(search_content: String): Int

    @Query("DELETE FROM images_table WHERE search_content = :search_content")
    fun deleteBySearchContents(search_content: String)

    @Query("DELETE FROM images_table")
    suspend fun deleteTable()
}
