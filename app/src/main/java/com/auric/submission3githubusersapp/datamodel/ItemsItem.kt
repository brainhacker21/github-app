package com.auric.submission3githubusersapp.datamodel

import com.google.gson.annotations.SerializedName

data class ItemsItem(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String)
