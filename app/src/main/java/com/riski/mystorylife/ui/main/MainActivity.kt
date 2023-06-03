package com.riski.mystorylife.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.clans.fab.FloatingActionMenu
import com.riski.mystorylife.AuthViewModel
import com.riski.mystorylife.R
import com.riski.mystorylife.ViewModelFactory
import com.riski.mystorylife.adapter.LoadStateAdapter
import com.riski.mystorylife.adapter.StoryAdapter
import com.riski.mystorylife.data.model.UserDataStory
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.databinding.ActivityMainBinding
import com.riski.mystorylife.ui.login.LoginActivity
import com.riski.mystorylife.ui.maps.MapsActivity
import com.riski.mystorylife.ui.setting.SettingActivity
import com.riski.mystorylife.ui.upload.UploadActivity
import java.util.*
import kotlin.concurrent.schedule

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: AuthViewModel
    private lateinit var storyViewModel: MainViewModel
    private lateinit var user: UserDataStory
    private lateinit var adapter: StoryAdapter
    private lateinit var fabMenu: FloatingActionMenu

    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.name_main)
        }

        recyclerView()
        setFloatingMenu()
        login()
        binding.refresh.setOnRefreshListener {
            mainviewModel1()
        }
        mainviewModel1()
        onRefresh()
    }

    private fun login() {
        val pref = Preference.getInstance(dataStore)
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref, this))[AuthViewModel::class.java]
        mainViewModel.getUser().observe(this) { user ->
            this.user = user
            if (!user.isLogin){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                this.user = UserDataStory(
                    user.userId,
                    user.name,
                    user.token,
                    true
                )
            }
        }
    }

    private fun mainviewModel1() {
        binding.refresh.isRefreshing = true
            storyViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore),this))[MainViewModel::class.java]
        storyViewModel.getUser().observe(this) { user ->
            storyViewModel.getStory(user.token).observe(this) {
                adapter.submitData(lifecycle, it)
            }
            }
        binding.refresh.isRefreshing = false
        }

    private fun recyclerView() {
        adapter = StoryAdapter()
        binding.recyclerMain.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerMain.adapter = adapter.withLoadStateFooter(
            footer = LoadStateAdapter {
                adapter.retry()
            })
    }

    private fun onRefresh() {
        binding.refresh.isRefreshing = true
        adapter.refresh()
        Timer().schedule(1000) {
            binding.refresh.isRefreshing = false
            binding.recyclerMain.smoothScrollToPosition(0)
        }
    }

    @Suppress("DEPRECATION")
    private fun setFloatingMenu() {
        fabMenu = findViewById(R.id.fab_menu)
        fabMenu.isIconAnimated = false
        fabMenu.setOnMenuToggleListener(object : FloatingActionMenu.OnMenuToggleListener {
            override fun onMenuToggle(opened: Boolean) {
            }
        })

        val fabItem1: com.github.clans.fab.FloatingActionButton = findViewById(R.id.menu1)
        fabItem1.setImageResource(R.drawable.ic_baseline_upload_24)
        fabItem1.setOnClickListener {
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        val fabItem2: com.github.clans.fab.FloatingActionButton = findViewById(R.id.menu2)
        fabItem2.setImageResource(R.drawable.ic_baseline_location_on_24)
        fabItem2.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            }
        }
        fabMenu.close(true)
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectMode: Int) {
        when (selectMode) {
            R.id.settings -> {
                val setting = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(setting)
            }
        }
    }

}