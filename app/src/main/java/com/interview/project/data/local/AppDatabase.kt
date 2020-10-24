package com.interview.project.data.local

import androidx.paging.DataSource
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

    @Query("SELECT * FROM images_table ORDER BY datetime DESC")
    fun imagesByDate(): DataSource.Factory<Int, Images>

    @Insert
    suspend fun insert(images: List<Images>)

    @Query("DELETE FROM images_table")
    suspend fun deleteTable()
}
