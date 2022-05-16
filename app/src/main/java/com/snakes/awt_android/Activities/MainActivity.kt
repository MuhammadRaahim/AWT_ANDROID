package com.snakes.awt_android

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.horizam.skbhub.Utils.Constants.Companion.awtHomeUrl
import com.jdars.shared_online_business.CallBacks.DrawerHandler
import com.snakes.awt_android.Utils.BaseUtils.Companion.loadWebView
import com.snakes.awt_android.Utils.BaseUtils.Companion.phoneIntent
import com.snakes.awt_android.databinding.ActivityMainBinding
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.LocationSource
import com.google.android.material.snackbar.Snackbar
import com.horizam.skbhub.Utils.Constants.Companion.awtAboutUsUrl
import com.horizam.skbhub.Utils.Constants.Companion.awtFacebookUrl
import com.horizam.skbhub.Utils.Constants.Companion.awtLinkedinUrl
import com.horizam.skbhub.Utils.Constants.Companion.emailAwt


class MainActivity : AppCompatActivity(), DrawerHandler {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI()
        setClickListener()
    }

    private fun setClickListener() {
        binding.apply {
            ivPhone.setOnClickListener {
                phoneIntent(this@MainActivity)
            }
            ivMail.setOnClickListener {
                composeEmail(emailAwt)
            }
            ivWeb.setOnClickListener {
                loadWebView(this@MainActivity, awtHomeUrl)
            }
            ivFacebook.setOnClickListener {
                loadWebView(this@MainActivity, awtFacebookUrl)
            }
            ivLinkedin.setOnClickListener {
                loadWebView(this@MainActivity, awtLinkedinUrl)
            }
            navAboutUs.setOnClickListener {
                loadWebView(this@MainActivity, awtAboutUsUrl)
            }
            navProfile.setOnClickListener {
                navController.navigate(R.id.navigation_profile)
            }

        }
    }

    private fun setUpUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navController = navHostFragment.navController

        getLocationPermission()
    }

    private fun getLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {

            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                showSnackBar(
                    binding.root,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.str_ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(this,getString(R.string.permission_required)
                    .plus(". Please enable it settings"), Toast.LENGTH_SHORT).show()

            }
        }

    override fun openDrawer() {
        binding.dlDrawer.openDrawer(GravityCompat.START)
    }

    private fun composeEmail(addresses: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.type = "text/plain"
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun showSnackBar(
        view: View, msg: String, length: Int, actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this.findViewById(android.R.id.content))
            }.show()
        } else {
            snackbar.show()
        }
    }

}