package com.auric.submission3githubusersapp.datamodel

import com.google.gson.annotations.SerializedName

data class GithubResponse(
	@field:SerializedName("items")
	val items: ArrayList<ItemsItem>
)
