package com.riski.mystorylife.ui.splashscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.riski.mystorylife.AuthViewModel
import com.riski.mystorylife.ViewModelFactory
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.databinding.ActivitySplashScreenBinding
import com.riski.mystorylife.ui.login.LoginActivity
import com.riski.mystorylife.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var splashViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        supportActionBar?.hide()

        goAnimation()
        installSplashScreen()
        saveLogin()
    }

    private fun saveLogin() {
        splashViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore),this))[AuthViewModel::class.java]

        splashViewModel.getUser().observe(this) {
            if (it.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }

    private fun goAnimation() {
        val rotateAnimation = ObjectAnimator.ofFloat(binding.imgSplash, View.ROTATION, 0f, 180f)
            rotateAnimation.duration = 2000
            rotateAnimation.repeatCount = ObjectAnimator.INFINITE
            rotateAnimation.repeatMode = ObjectAnimator.RESTART

        val translationAnimation = ObjectAnimator.ofFloat(binding.imgSplash, View.TRANSLATION_Y, 0f,-200f)
            translationAnimation.duration = 2000
            translationAnimation.repeatCount = ObjectAnimator.INFINITE
            translationAnimation.repeatMode = ObjectAnimator.REVERSE

        val animationSet = AnimatorSet()
        animationSet.playTogether(rotateAnimation, translationAnimation)
        animationSet.start()
    }

}