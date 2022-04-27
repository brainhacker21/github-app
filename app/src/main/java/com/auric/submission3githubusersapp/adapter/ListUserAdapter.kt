package com.auric.submission3githubusersapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.auric.submission3githubusersapp.datamodel.ItemsItem
import com.auric.submission2_aplikasigithubuser.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(){
    private var oldList = emptyList<ItemsItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(newList:List<ItemsItem>){
        val diffUtil = MyDiffUtil(oldList,newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsItem){
            binding.root.setOnClickListener{
                onItemClickCallback.onItemClicked(item)
            }
            binding.apply{
                Glide.with(itemView)
                    .load(item.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(imgItemAvatar)
                tvItemUsername.text = item.login
            }
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    interface OnItemClickCallback {
        fun onItemClicked(item: ItemsItem)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(oldList[position])
    }

    override fun getItemCount(): Int = oldList.size
}