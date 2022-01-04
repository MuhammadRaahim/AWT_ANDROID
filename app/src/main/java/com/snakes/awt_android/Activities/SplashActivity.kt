package com.snakes.awt_android.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSplash()
        setSplashImage()
    }

    private fun setSplashImage() {
        Glide
            .with(this)
            .load(R.raw.splash)
            .into(binding.ivLogo)
    }

    private fun setSplash() {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }, Constants.SPLASH_DISPLAY_LENGTH.toLong())
    }

}