package com.auric.submission3githubusersapp.database

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auric.submission2_aplikasigithubuser.databinding.ActivityFavoriteBinding
import com.auric.submission3githubusersapp.adapter.ListUserAdapter
import com.auric.submission3githubusersapp.datamodel.ItemsItem
import com.auric.submission3githubusersapp.ui.DetailUserActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var rvUser: RecyclerView
    private var _binding: ActivityFavoriteBinding? = null
    private val  binding get() = _binding!!
    private val listUserAdapter= ListUserAdapter()
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsers.adapter = listUserAdapter

        supportActionBar?.title = "Favorite User"
        supportActionBar?.setDisplayShowHomeEnabled(true)

        rvUser = binding.rvUsers
        rvUser.setHasFixedSize(true)

        viewModel.getFavorite()

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(item: ItemsItem) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, item.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, item.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, item.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.rvUsers.layoutManager = GridLayoutManager(this, 2)
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvUsers.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvUsers.layoutManager = LinearLayoutManager(this)
        }
        viewModel.getFavorite()?.observe(this) {
            if (it!=null){
                val list = mapList(it)
                listUserAdapter.setData(list)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun mapList(items: List<Favorite>): ArrayList<ItemsItem> {
        val listUsers = ArrayList<ItemsItem>()
        for (item in items) {
            val itemMapped = ItemsItem(
                item.login,
                item.id,
                item.avatarUrl
            )
            listUsers.add(itemMapped)
        }
    return listUsers
    }
}