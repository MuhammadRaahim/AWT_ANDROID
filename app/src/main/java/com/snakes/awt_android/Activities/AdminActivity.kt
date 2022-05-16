package com.snakes.awt_android.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.load.resource.gif.GifDrawableEncoder
import com.google.android.material.snackbar.Snackbar
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity(),GenericHandler {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI()
    }

    private fun setUpUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.admin_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun showProgressBar(show: Boolean) {
        binding.progressLayout.isVisible = show
    }

    override fun showMessage(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showNoInternet(show: Boolean) {

    }
}