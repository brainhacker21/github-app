package com.auric.submission3githubusersapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.auric.submission2_aplikasigithubuser.R
import com.auric.submission3githubusersapp.datastore.Darkmode
import com.auric.submission3githubusersapp.datastore.DarkModeViewModel
import com.auric.submission3githubusersapp.datastore.DarkModeViewModelFactory
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@SuppressLint("CustomSplashScreen")
class Splashscreenactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@Splashscreenactivity, MainActivity::class.java))
            finish()
        }, 0)

        val pref = Darkmode.getInstance(dataStore)
        val darkViewModel = ViewModelProvider(this, DarkModeViewModelFactory(pref)).get(
            DarkModeViewModel::class.java
        )

        darkViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                darkViewModel.saveThemeSetting(true)
                }
             else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                darkViewModel.saveThemeSetting(false)
                }
            }
        }
    }