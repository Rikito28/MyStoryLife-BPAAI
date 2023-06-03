package com.riski.mystorylife.ui.setting

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.riski.mystorylife.AuthViewModel
import com.riski.mystorylife.R
import com.riski.mystorylife.ViewModelFactory
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.databinding.ActivitySettingBinding
import com.riski.mystorylife.ui.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    private lateinit var settingViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.setting_app)
            setDisplayHomeAsUpEnabled(true)
        }

        settingViewModel1()
        onClicked()
    }

    private fun settingViewModel1() {
        settingViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore),this))[AuthViewModel::class.java]
    }

    private fun onClicked() {
        binding.settingLogout1.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.sure))
                setPositiveButton(getString(R.string.logout)) { _, _ ->
                    settingViewModel.logout()
                    startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                    finishAffinity()
                    finish()
                }
                setNegativeButton(getString(R.string.cancel)) { _, _ ->
                }
                create()
                show()
            }
        }

        binding.settingLang.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}