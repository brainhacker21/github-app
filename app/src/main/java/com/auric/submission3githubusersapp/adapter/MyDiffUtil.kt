package com.auric.submission3githubusersapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.auric.submission3githubusersapp.datamodel.ItemsItem

open class MyDiffUtil(
        private val oldList: List<ItemsItem>,
        private val newList: List<ItemsItem>,
    ): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].login == newList[newItemPosition].login
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].login == newList[newItemPosition].login
    }
}
