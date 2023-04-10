package com.example.home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.models.Genre
import kotlinx.coroutines.flow.Flow

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@Dao
interface GenresDao {
    @Query("SELECT * FROM genres")
    fun getAllGenres(): Flow<List<Genre>>

    @Query("DELETE FROM genres")
    fun deleteAll()

    @Insert
    fun insertAllGenres(data: List<Genre>)

    @Query("SELECT * FROM genres WHERE id = :id")
    fun getGenre(id: Int): Flow<Genre>

    @Transaction
    fun replaceTable(data: List<Genre>) {
        deleteAll()
        insertAllGenres(data)
    }
}
