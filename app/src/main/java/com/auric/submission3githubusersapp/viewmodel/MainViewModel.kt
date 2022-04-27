package com.auric.submission3githubusersapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auric.submission2_aplikasigithubuser.BuildConfig
import com.auric.submission3githubusersapp.api.ApiConfig
import com.auric.submission3githubusersapp.datamodel.GithubResponse
import com.auric.submission3githubusersapp.datamodel.ItemsItem
import com.auric.submission3githubusersapp.datastore.Darkmode
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel() : ViewModel() {

    private val _item = MutableLiveData<List<ItemsItem>>()
    val item: LiveData<List<ItemsItem>> = _item

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isText = MutableLiveData<Boolean>()
    val isText: LiveData<Boolean> = _isText

    init {
        finduser(query)
    }

    fun finduser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUsers(  query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                _isText.value = true
                if (response.isSuccessful) {
                    _isText.value = false
                    _item.value = response.body()?.items
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
    companion object {
        private const val TAG = "MainViewModel"
        private const val query = ""
    }
}
