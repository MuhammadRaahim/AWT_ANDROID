package com.snakes.awt_android.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.horizam.skbhub.Utils.Constants.Companion.SERVICE_DATABASE_ROOT
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.compressFile
import com.snakes.awt_android.Utils.BaseUtils.Companion.showMessage
import com.snakes.awt_android.Utils.ImageFilePath
import com.snakes.awt_android.databinding.ActivityCreateServiceBinding
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import com.google.android.libraries.places.api.Places

import com.google.android.libraries.places.api.net.PlacesClient



class CreateServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateServiceBinding
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var serviceReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private var imagePath: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setClickListeners()

    }

    private fun setClickListeners() {
        binding.apply {
            ivEditImage.setOnClickListener {
                launchGalleryIntent()
            }
            btnCreateService.setOnClickListener {
                when {
                    etServiceName.text.toString().trim().isEmpty() -> {
                        etServiceName.error = getString(R.string.str_invalid_field)
                    }
                    etDescriptions.text.toString().trim().isEmpty() -> {
                        etDescriptions.error = getString(R.string.str_invalid_field)
                    }
                    etDetails.text.toString().trim().isEmpty() -> {
                        etDetails.error = getString(R.string.str_invalid_field)
                    }
                    else -> {
                        binding.progressLayout.isVisible = true
                        getData()
                    }
                }
            }
        }
    }

    private fun getData() {
        if (imagePath!= null){
            uploadImageToStorage(imagePath!!)
        }else{
            addService("")
        }
    }

    private fun uploadImageToStorage(imagePath: String) {
        lifecycleScope.launch {
            val file = File(compressFile(imagePath))
            val uniqueId = UUID.randomUUID().toString()
            val storagePath = "Service Images/".plus(uniqueId)
            uploadFile(file, storagePath)
        }
    }

    private fun uploadFile(file: File, storagePath: String) {
        val ext: String = file.extension
        if (ext.isEmpty()) {
            showMessage(findViewById(android.R.id.content),"Something went wrong")
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
                        addService(uri.toString())
                    }
                }
            }
        }.addOnProgressListener { taskSnapshot ->
            binding.progressLayout.visibility = View.VISIBLE
        }.addOnFailureListener { e ->
            binding.progressLayout.visibility = View.GONE
            showMessage(findViewById(android.R.id.content),e.message.toString())
        }
    }

    private fun addService(image: String) {
        binding.apply {
            val refId  = serviceReference.document()
            val title = etServiceName.text.toString().trim()
            val description = etDescriptions.text.toString().trim()
            val details = etDetails.text.toString().trim()

            val service = Service(
                id = refId.id, serviceName = title, description = description,
                details = details, donation = 0,serviceImage = image
            )
            uploadService(service)
        }
    }

    private fun uploadService(service: Service) {
        val ref = serviceReference.document(service.id!!)
        ref.set(service).addOnSuccessListener {
            binding.progressLayout.isVisible = false
            Toast.makeText(this,"Service added Successfully",Toast.LENGTH_SHORT).show()
            onBackPressed()
        }.addOnFailureListener{
            binding.progressLayout.isVisible = false
            Toast.makeText(this,it.message.toString(),Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        serviceReference = db.collection(SERVICE_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()
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