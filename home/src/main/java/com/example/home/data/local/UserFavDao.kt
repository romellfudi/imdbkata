package com.example.home.data.local

import androidx.room.*
import com.example.data.models.UserFav
import kotlinx.coroutines.flow.Flow

/**
 * @author @romellfudi
 * @date 2023-03-17
 * @version 1.0.a
 */
@Dao
interface UserFavDao {

    @Query("SELECT * FROM user_fav WHERE userMail = :userMail")
    fun getAllFavBy(userMail: String): Flow<List<UserFav>>

    @Query("DELETE FROM user_fav WHERE userMail = :userMail AND movieId = :movieId")
    fun deleteFav(userMail: String, movieId: Int): Int

    @Query("DELETE FROM user_fav where userMail = 'guest'")
    fun deleteGuestFav(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFav(userFav: UserFav): Long
}
