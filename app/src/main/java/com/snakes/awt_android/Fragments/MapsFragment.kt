package com.snakes.awt_android.Fragments

import android.Manifest

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.snackbar.Snackbar
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentMapsBinding


class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var dasterKhawanBottomFragment: DasterKhawanBottomFragment
    private var lastKnownLocation: Location? = null
    private var locationPermissionGranted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(layoutInflater)
         checkLocation()
//        setUpUI()

        return binding.root
    }

    private fun setUpUI() {
        setMap()
        binding.toolbar.ivDonate.visibility = View.GONE
        binding.toolbar.ivGraph.visibility = View.GONE

        Places.initialize(requireActivity().applicationContext, getString(R.string.API_KEY))
        placesClient = Places.createClient(requireContext())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLocationPermission()

        dasterKhawanBottomFragment = DasterKhawanBottomFragment()
        dasterKhawanBottomFragment.show(requireActivity().supportFragmentManager,"readTag")
    }

    private fun setMap() {
        val mapFragment = childFragmentManager.
        findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
//        updateLocationUI()
        getDeviceLocation()
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                         lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), 14.0f))
                            mMap.addMarker(MarkerOptions().position(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)))
                        }
                    } else {
                        val sydney = LatLng(30.3753, 69.3451)
                        mMap.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(sydney, 14.0f))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                locationPermissionGranted = true
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
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
                Toast.makeText(requireContext(),getString(R.string.permission_required)
                    .plus(". Please enable it settings"), Toast.LENGTH_SHORT).show()
                locationPermissionGranted = false
            }else{
                locationPermissionGranted = false
            }
        }

    private fun showSnackBar(
        view: View, msg: String, length: Int, actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(requireView().findViewById(android.R.id.content))
            }.show()
        } else {
            snackbar.show()
        }
    }

    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
//                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun checkLocation(){
        val lm = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder(context)
                .setMessage("GPS is not enable")
                .setPositiveButton("open setting",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Cancel", null)
                .show()
        }
    }



}