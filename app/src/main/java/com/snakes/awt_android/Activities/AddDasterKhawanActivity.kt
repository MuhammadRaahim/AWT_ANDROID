package com.snakes.awt_android.Activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils
import com.snakes.awt_android.Utils.BaseUtils.Companion.hideKeyboard
import com.snakes.awt_android.Utils.BaseUtils.Companion.showDatePicker
import com.snakes.awt_android.Utils.BaseUtils.Companion.showTimePicker
import com.snakes.awt_android.Utils.ImageFilePath
import com.snakes.awt_android.databinding.ActivityAddDasterKhawanBinding
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class AddDasterKhawanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDasterKhawanBinding
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var dasterKhawanReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var apiKey: String
    private var imagePath: String? = null
    private lateinit var latLng: LatLng

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDasterKhawanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setClickListeners()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setClickListeners() {
        binding.apply {
            ivEditImage.setOnClickListener {
                launchGalleryIntent()
            }
            ivBack.setOnClickListener {
                onBackPressed()
            }
            etDate.setOnClickListener {
                showDatePicker(etDate,this@AddDasterKhawanActivity)
            }
            etStartTime.setOnClickListener {
                showTimePicker(etStartTime,this@AddDasterKhawanActivity)
            }
            etEndTime.setOnClickListener {
                showTimePicker(etEndTime,this@AddDasterKhawanActivity)
            }
            etLocation.setOnClickListener {
                getAddress()
            }
            btnAddDasterKhawan.setOnClickListener {
                hideKeyboard()
                validateData()
            }
        }
    }

    private fun validateData() {
        binding.apply {
            when {
                etServiceName.text.isNullOrEmpty() -> {
                    etServiceName.requestFocus()
                    etServiceName.error = getString(R.string.str_fields_require)
                    return
                }
                etDescriptions.text.isNullOrEmpty() -> {
                    etDescriptions.requestFocus()
                    etDescriptions.error = getString(R.string.str_fields_require)
                    return
                }
                etDate.text.isNullOrEmpty() -> {
                    etDate.requestFocus()
                    etDate.error = getString(R.string.str_fields_require)
                    return
                }
                etStartTime.text.isNullOrEmpty() -> {
                    etStartTime.requestFocus()
                    etStartTime.error = getString(R.string.str_fields_require)
                    return
                }
                etEndTime.text.isNullOrEmpty() -> {
                    etEndTime.requestFocus()
                    etEndTime.error = getString(R.string.str_fields_require)
                    return
                }
                etLocation.text.isNullOrEmpty() -> {
                    etLocation.requestFocus()
                    etLocation.error = getString(R.string.str_fields_require)
                    return
                }
                etDescriptions.text.isNullOrEmpty() -> {
                    etDescriptions.requestFocus()
                    etDescriptions.error = getString(R.string.str_fields_require)
                    return
                }
                else -> {
                    binding.progressLayout.visibility = View.VISIBLE
                    getData()
                }
            }
        }
    }

    private fun getData() {
        if (imagePath!= null){
            uploadImageToStorage(imagePath!!)
        }else{
            addDasterKhawan("")
        }
    }

    private fun uploadImageToStorage(imagePath: String) {
        lifecycleScope.launch {
            val file = File(BaseUtils.compressFile(imagePath))
            val uniqueId = UUID.randomUUID().toString()
            val storagePath = "DasterKhawan Images/".plus(uniqueId)
            uploadFile(file, storagePath)
        }
    }

    private fun uploadFile(file: File, storagePath: String) {
        val ext: String = file.extension
        if (ext.isEmpty()) {
            BaseUtils.showMessage(findViewById(android.R.id.content), "Something went wrong")
            return
        }
        val storageReference = firebaseStorage.reference.child("$storagePath.$ext")
        val uriFile = Uri.fromFile(file)
        val uploadTask: UploadTask = storageReference.putFile(uriFile)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            if (taskSnapshot.metadata != null && taskSnapshot.metadata!!.reference != null) {
                val result = taskSnapshot.storage.downloadUrl
                result.addOnSuccessListener { uri ->
                    if (uri != null) {
                        addDasterKhawan(uri.toString())
                    }
                }
            }
        }.addOnProgressListener { taskSnapshot ->
            binding.progressLayout.visibility = View.VISIBLE
        }.addOnFailureListener { e ->
            binding.progressLayout.visibility = View.GONE
            BaseUtils.showMessage(findViewById(android.R.id.content), e.message.toString())
        }
    }

    private fun addDasterKhawan(image: String) {
        binding.apply {
            val refId  = dasterKhawanReference.document()
            val dasterKhawan = DasterKhawan(
                id = refId.id,
                name = etServiceName.text.toString().trim(),
                description = etDescriptions.text.toString().trim(),
                date = etDate.text.toString().trim(),
                startTime = etStartTime.text.toString().trim(),
                endTime = etEndTime.text.toString().trim(),
                locatoion = etLocation.text.toString().trim(),
                lat = latLng.latitude,
                long = latLng.longitude,
                details = etDetails.text.toString().trim(),
                photo = image
            )
            uploadService(dasterKhawan)
        }
    }

    private fun uploadService(dasterKhawan: DasterKhawan) {
        val ref = dasterKhawanReference.document(dasterKhawan.id!!)
        ref.set(dasterKhawan).addOnSuccessListener {
            binding.progressLayout.isVisible = false
            Toast.makeText(this,"DasterKhawan added Successfully",Toast.LENGTH_SHORT).show()
            onBackPressed()
        }.addOnFailureListener{
            binding.progressLayout.isVisible = false
            Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        apiKey = "AIzaSyCMR63Gt-Iwztwwt4dtpgwicYXq27oI03k"
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        dasterKhawanReference = db.collection(Constants.DASTERKHAWAN_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()
    }

    private fun getAddress() {
        val fields = listOf(Place.Field.LAT_LNG,Place.Field.ADDRESS,Place.Field.ID, Place.Field.NAME)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        startForResult.launch(intent)
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.let {
                        val place = Autocomplete.getPlaceFromIntent(it)
                        latLng = place.latLng!!
                        binding.etLocation.setText(place.address)
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    result.data?.let {
                        val status = Autocomplete.getStatusFromIntent(it)
                        Toast.makeText(this,status.statusMessage,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun launchGalleryIntent() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                getImageFromGallery.launch("image/*")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                showSnackBar(
                    binding.root,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    "Ok"
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
                getImageFromGallery.launch("image/*")
            } else {
                Log.i("Permission: ", "Denied")
                Toast.makeText(this,getString(R.string.permission_required)
                    .plus(". Please enable it settings"), Toast.LENGTH_SHORT).show()
            }
        }

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        setPicture(uri)
    }

    private fun showSnackBar(
        view: View, msg: String, length: Int, actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackBar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackBar.setAction(actionMessage) {
                action(this.findViewById(android.R.id.content))
            }.show()
        } else {
            snackBar.show()
        }
    }

    private fun setPicture(uri: Uri?) {
        imagePath = ImageFilePath().getFilePath(uri!!,this)
        Glide.with(this).load(uri).placeholder(R.drawable.img_profile_cover_placeholder)
            .into(binding.ivEvent)
    }


}