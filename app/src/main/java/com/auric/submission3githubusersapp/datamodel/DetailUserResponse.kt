package com.auric.submission3githubusersapp.datamodel

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("company")
	val company: String,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("public_repos")
	val publicrepos: Int
)