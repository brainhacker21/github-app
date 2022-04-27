package com.auric.submission3githubusersapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.auric.submission2_aplikasigithubuser.R
import com.auric.submission3githubusersapp.adapter.SectionsPagerAdapter
import com.auric.submission2_aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.auric.submission3githubusersapp.database.Favorite
import com.auric.submission3githubusersapp.database.FavoriteViewModel
import com.auric.submission3githubusersapp.datamodel.ItemsItem
import com.auric.submission3githubusersapp.viewmodel.DetailUserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID,0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(
                this
        )[DetailUserViewModel::class.java]

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.setUserDetail(username!!)
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    tvItemName.text = it.name
                    tvItemUsername.text = it.login
                    tvViewFollower.text = "${it.followers}"
                    tvViewFollowing.text = "${it.following}"
                    tvItemRepository.text ="${it.publicrepos}"
                    tvItemCompany.text = it.company?: "-"
                    tvItemLocation.text = it.location?: "-"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .circleCrop()
                        .into(imgItemAvatar)
                    showLoading(false)
                }
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.fvuser.isChecked = true
                        _isChecked = true

                    } else {
                        binding.fvuser.isChecked = false
                        _isChecked = false

                    }
                }
            }
        }

       binding.fvuser.setOnClickListener{
            _isChecked = !_isChecked
            if (_isChecked){
                viewModel.addfavorite(username,id,avatarUrl!!)
                Toast.makeText(applicationContext, "Favorited", Toast.LENGTH_SHORT).show()
            }else{
                viewModel.removefavorite(username,id,avatarUrl!!)
                Toast.makeText(applicationContext, "Delete", Toast.LENGTH_SHORT).show()
            }
            binding.fvuser.isChecked = _isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.explores -> {
                val exploreuser = "https://www.github.com/${binding.tvItemUsername.text}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(exploreuser))
                startActivity(intent)
            }
        }
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }
}