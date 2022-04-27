package com.auric.submission3githubusersapp.api

import com.auric.submission2_aplikasigithubuser.BuildConfig
import com.auric.submission3githubusersapp.datamodel.DetailUserResponse
import com.auric.submission3githubusersapp.datamodel.GithubResponse
import com.auric.submission3githubusersapp.datamodel.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getSearchUsers(
        @Query("q") query: String

    ): Call<GithubResponse>

   @GET("users/{username}")
   @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
   fun getUserDetail(
       @Path("username") username: String
   ): Call<DetailUserResponse>


   @GET("users/{username}/followers")
   @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
   fun getFollowers(
       @Path("username") username: String
   ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
    }