package com.auric.submission3githubusersapp.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.auric.submission3githubusersapp.viewmodel.DetailUserViewModel

class FavoriteViewModel(application: Application) : AndroidViewModel(application){
    private var userDao: FavoriteDao?
    private var userDB: FavoriteRoomDatabase?

    init {
        userDB = FavoriteRoomDatabase.getDatabase(application)
        userDao = userDB?.favoriteDao()
    }
    fun getFavorite(): LiveData<List<Favorite>>? {
        return userDao?.getFavorite()
    }
}