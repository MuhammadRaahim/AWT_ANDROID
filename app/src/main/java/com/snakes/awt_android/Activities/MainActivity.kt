package com.snakes.awt_android

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.Constants.Companion.awtAboutUsUrl
import com.horizam.skbhub.Utils.Constants.Companion.awtFacebookUrl
import com.horizam.skbhub.Utils.Constants.Companion.awtHomeUrl
import com.horizam.skbhub.Utils.Constants.Companion.awtLinkedinUrl
import com.horizam.skbhub.Utils.Constants.Companion.awtOrphanHouseUrl
import com.horizam.skbhub.Utils.Constants.Companion.awtOurProjectsUrl
import com.horizam.skbhub.Utils.Constants.Companion.emailAwt
import com.horizam.skbhub.Utils.PrefManager
import com.jdars.shared_online_business.CallBacks.DrawerHandler
import com.snakes.awt_android.Activities.AuthActivity
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.Utils.BaseUtils
import com.snakes.awt_android.Utils.BaseUtils.Companion.loadWebView
import com.snakes.awt_android.Utils.BaseUtils.Companion.phoneIntent
import com.snakes.awt_android.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), DrawerHandler, GenericHandler{

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: PrefManager
    private lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpUI()
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        checkInternet()
    }

    private fun setClickListener() {
        binding.apply {
            layoutNoInternet.btnClose.setOnClickListener {
                finish()
            }
            layoutNoInternet.btnRetry.setOnClickListener {
                checkInternet()
            }
            ivPhone.setOnClickListener {
                drawer.closeDrawers()
                phoneIntent(this@MainActivity)
            }
            ivMail.setOnClickListener {
                drawer.closeDrawers()
                composeEmail(emailAwt)
            }
            ivWeb.setOnClickListener {
                drawer.closeDrawers()
                loadWebView(this@MainActivity, awtHomeUrl)
            }
            ivFacebook.setOnClickListener {
                drawer.closeDrawers()
                loadWebView(this@MainActivity, awtFacebookUrl)
            }
            ivLinkedin.setOnClickListener {
                drawer.closeDrawers()
                loadWebView(this@MainActivity, awtLinkedinUrl)
            }
            navHome.setOnClickListener {
                drawer.closeDrawers()
                when {
                    navController.currentDestination!!.id != R.id.navigation_home ->
                        navController . navigate (R.id.navigation_home)
                }
            }
            navAboutUs.setOnClickListener {
                drawer.closeDrawers()
                loadWebView(this@MainActivity, awtAboutUsUrl)
            }
            navProfile.setOnClickListener {
                drawer.closeDrawers()
                when {
                    navController.currentDestination!!.id != R.id.navigation_profile ->
                        navController . navigate (R.id.navigation_profile)
                }
            }
            navOurProject.setOnClickListener {
                drawer.closeDrawers()
                loadWebView(this@MainActivity, awtOurProjectsUrl)
            }
            navOrphanHouse.setOnClickListener {
                drawer.closeDrawers()
                loadWebView(this@MainActivity, awtOrphanHouseUrl)
            }
            navRateUs.setOnClickListener {
                drawer.closeDrawers()
                showMessage("App is not Publish yet!!")

            }
            navLogout.setOnClickListener {
                logOut()
            }
        }
    }

    private fun logOut() {
        drawer.closeDrawers()
        AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Logout")
            .setMessage("Are you sure you want to Logout")
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    manager.session = Constants.LOGOUT
                    startActivity(Intent(this@MainActivity,AuthActivity::class.java))
                    finish()
                })
            .setNegativeButton("No", null)
            .show()
    }

    private fun setUpUI() {
        drawer = binding.dlDrawer
        manager = PrefManager(this)
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
        drawer.openDrawer(GravityCompat.START)
    }

    private fun composeEmail(addresses: String) {
        val email = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(addresses))
            putExtra(Intent.EXTRA_SUBJECT, "Subject Text Here..")
            putExtra(Intent.EXTRA_TEXT, "")
            type = "message/rfc822"
        }
        startActivity(Intent.createChooser(email, "Send Mail Using :"))
    }

    private fun showSnackBar(
        view: View, msg: String, length: Int, actionMessage: CharSequence?,
        action: (View) -> Unit
    ){
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this.findViewById(android.R.id.content))
            }.show()
        } else {
            snackbar.show()
        }
    }

    override fun showProgressBar(show: Boolean) {
        binding.layoutNoInternet.dialogParent.isVisible = show
    }

    override fun showMessage(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showNoInternet(show: Boolean) {
        binding.layoutNoInternet.dialogParent.isVisible = show
    }

    private fun checkInternet() {
        showNoInternet(!BaseUtils.isInternetAvailable(this))
    }

}