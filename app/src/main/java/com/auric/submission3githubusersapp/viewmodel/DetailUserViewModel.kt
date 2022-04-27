package com.auric.submission3githubusersapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.auric.submission3githubusersapp.api.ApiConfig
import com.auric.submission3githubusersapp.database.Favorite
import com.auric.submission3githubusersapp.database.FavoriteDao
import com.auric.submission3githubusersapp.database.FavoriteRoomDatabase
import com.auric.submission3githubusersapp.datamodel.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetailUserViewModel(application:Application): AndroidViewModel(application) {

    private val _user = MutableLiveData<DetailUserResponse>()
    private val user: LiveData<DetailUserResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var userDao: FavoriteDao?
    private var userDB: FavoriteRoomDatabase?

    init {
        setUserDetail(username)
        userDB = FavoriteRoomDatabase.getDatabase(application)
        userDao = userDB?.favoriteDao()
    }

    fun setUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.postValue(response.body())
                } else {
                    Log.e(TAG, "onFailure: ${response.message() + "terjadi kesalahan"}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message} + terjadi kesalahan ")
            }
        })
    }

    fun getUserDetail() : LiveData<DetailUserResponse> {
        return user
    }

    fun addfavorite(username: String,id :Int, avatarUrl : String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = Favorite(
                username,
                id,
            avatarUrl)
            userDao?.addfavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removefavorite(username: String,id :Int, avatarUrl : String){
        CoroutineScope(Dispatchers.IO).launch {
            var user = Favorite(
                username,
                id,
                avatarUrl)
            userDao?.removefavorite(user)
        }
    }

    companion object{
        private const val TAG = "DetailUserViewModel"
        private const val username = ""
    }
}