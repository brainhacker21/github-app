package com.auric.submission3githubusersapp.ui


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auric.submission2_aplikasigithubuser.R
import com.auric.submission3githubusersapp.adapter.ListUserAdapter
import com.auric.submission2_aplikasigithubuser.databinding.ActivityMainBinding
import com.auric.submission3githubusersapp.database.FavoriteActivity
import com.auric.submission3githubusersapp.datamodel.ItemsItem
import com.auric.submission3githubusersapp.datastore.Darkmode
import com.auric.submission3githubusersapp.datastore.DarkModeViewModel
import com.auric.submission3githubusersapp.datastore.DarkModeViewModelFactory
import com.auric.submission3githubusersapp.viewmodel.MainViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private lateinit var rvUser: RecyclerView
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val listUserAdapter = ListUserAdapter()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsers.adapter = listUserAdapter

        rvUser = binding.rvUsers
        rvUser.setHasFixedSize(true)

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(item: ItemsItem) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
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

        viewModel.item.observe(this@MainActivity) { item ->
            setUserResponse(item)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.isText.observe(this) {
            showText(it)
        }

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = binding.svUsers
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.cari_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.finduser(query)
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText == ""){
                    val listClear = ArrayList<ItemsItem>()
                    setUserResponse(listClear)
                    showLoading(false)
                    showText(true)
                }else{
                    viewModel.finduser(newText).toString()
                    showText(false)
                }
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pref = Darkmode.getInstance(dataStore)
        val darkViewModel = ViewModelProvider(this, DarkModeViewModelFactory(pref)).get(
            DarkModeViewModel::class.java
        )
        when (item.itemId) {
            R.id.favorite -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
                return true
            }
            R.id.menu -> {
                startActivity(Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.search -> {
                val listClear = ArrayList<ItemsItem>()
                setUserResponse(listClear)
                showText(true)
            }
            R.id.darkmode ->
                darkViewModel.getThemeSettings().observe(this) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    darkViewModel.saveThemeSetting(true)
                }

            R.id.lightmode ->
                darkViewModel.getThemeSettings().observe(this) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    darkViewModel.saveThemeSetting(false)
                }
        }
        return true
    }

    private fun setUserResponse(items: List<ItemsItem>) {
        listUserAdapter.setData(items)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
    private fun showText(isText: Boolean) {
        if (isText) {
            binding.welcome.visibility = View.VISIBLE
        } else {
            binding.welcome.visibility = View.GONE
        }
    }
}