package com.snakes.awt_android.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.PrefManager
import com.snakes.awt_android.App
import com.snakes.awt_android.MainActivity
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var manger: PrefManager


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
            checkLoginInfo()
        }, Constants.SPLASH_DISPLAY_LENGTH.toLong())
    }

    private fun checkLoginInfo(){
        manger = PrefManager(App.getAppContext()!!)
        var intent: Intent? = null
        when (manger.session) {
            Constants.ADMIN -> {
                intent = Intent(this,AdminActivity::class.java)
            }
            Constants.USER -> {
                intent = Intent(this,MainActivity::class.java)
            }
            else -> {
                intent = Intent(this,AuthActivity::class.java)
            }
        }
        startActivity(intent)
        finish()
    }

}