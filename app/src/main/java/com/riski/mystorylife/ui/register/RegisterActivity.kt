package com.riski.mystorylife.ui.register

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.riski.mystorylife.R
import com.riski.mystorylife.ViewModelFactory
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.databinding.ActivityRegisterBinding
import com.riski.mystorylife.helper.mailValid
import com.riski.mystorylife.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    private lateinit var binding: ActivityRegisterBinding

    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        regisViewModel()
        buttonenabled()
        editText()
        btnRegister()
        progressBar()
        goAnimation()

        binding.languageFab.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun buttonenabled() {
        binding.btnRegister.isEnabled = binding.registerEmail.text.toString().isNotEmpty() && binding.registerPassword.text.toString().isNotEmpty() && binding.registerPassword.text.toString().length >= 6 && mailValid(binding.registerEmail.text.toString())
    }

    private fun editText() {
        binding.registerEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                buttonenabled()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        binding.registerPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonenabled()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun regisViewModel() {
        registerViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore),this))[RegisterViewModel::class.java]
    }

    private fun btnRegister() {
        binding.btnRegister.setOnClickListener {
            val name = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()

            registerViewModel.register(name, email, password)
                registerViewModel.response.observe(this@RegisterActivity) { register ->
                    if (register != null) {
                        if (!register.error) {
                            progressBar()
                            Toast.makeText(
                                this@RegisterActivity,
                                getString(R.string.success_regis),
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            progressBar()
                            Toast.makeText(this@RegisterActivity, getString(R.string.failed_regis), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }}

    private fun goAnimation() {
        ObjectAnimator.ofFloat(binding.imgRegister, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun progressBar() {
        registerViewModel.loading.observe(this) {
            binding.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}