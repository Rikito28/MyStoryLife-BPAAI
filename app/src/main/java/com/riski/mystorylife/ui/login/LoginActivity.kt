package com.riski.mystorylife.ui.login

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
import com.riski.mystorylife.databinding.ActivityLoginBinding
import com.riski.mystorylife.helper.mailValid
import com.riski.mystorylife.ui.main.MainActivity
import com.riski.mystorylife.ui.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var loginViewModel : LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        loginUserViewModel()
        progressBar()
        btnLogin()
        editText()
        buttonEnabled()
        goAnimation()

        binding.languageFab.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.registerLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }

    private fun loginUserViewModel() {
        loginViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore),this))[LoginViewModel::class.java]
    }

    private fun editText() {
        binding.loginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                buttonEnabled()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        binding.loginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                buttonEnabled()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun buttonEnabled() {
        val resultMail = binding.loginEmail.text
        val resultPass = binding.loginPassword.text

        binding.btnLogin.isEnabled = resultPass != null && resultMail != null && binding.loginPassword.text.toString().length >= 8 && mailValid(binding.loginEmail.text.toString())
    }

    private fun btnLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            loginViewModel.login(email, password)
            loginViewModel.response.observe(this@LoginActivity) { response ->
                if (response != null) {
                    if (!response.error) {
                        progressBar()
                        Toast.makeText(this@LoginActivity, getString(R.string.success_login), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    else {
                        progressBar()
                        Toast.makeText(this@LoginActivity, getString(R.string.failed_login), Toast.LENGTH_SHORT).show()
                    }
                }
            }
                }
            }

    private fun goAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X,-40f,40f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun progressBar() {
        loginViewModel.loading.observe(this) {
            binding.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    btnLogin.visibility = View.INVISIBLE
                }
                else progressBar.visibility = View.GONE
            }
        }
    }
}