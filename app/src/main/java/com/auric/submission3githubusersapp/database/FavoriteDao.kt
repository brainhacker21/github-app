package com.auric.submission3githubusersapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
        @Insert
        suspend fun addfavorite(favorite: Favorite)

        @Query("SELECT * FROM favorite_user")
        fun getFavorite(): LiveData<List<Favorite>>

        @Query("SELECT count (*) FROM favorite_user WHERE favorite_user.id = :id")
        suspend fun checkUser(id:Int):Int

        @Delete
        suspend fun removefavorite(favorite: Favorite)


}