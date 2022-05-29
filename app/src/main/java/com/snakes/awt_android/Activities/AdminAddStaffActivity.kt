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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
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
import com.snakes.awt_android.Models.Admin
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.hideKeyboard
import com.snakes.awt_android.Utils.BaseUtils.Companion.showMessage
import com.snakes.awt_android.Utils.ImageFilePath
import com.snakes.awt_android.Utils.Validator
import com.snakes.awt_android.databinding.ActivityAdminAddStaffBinding
import java.io.File
import java.util.*

class AdminAddStaffActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddStaffBinding
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var staffReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setClickListeners()


    }

    private fun setClickListeners() {
        binding.apply {
            icBack.setOnClickListener {
                onBackPressed()
            }
            ivChangeProfile.setOnClickListener {
                launchGalleryIntent()
            }
            btnSaveStaff.setOnClickListener {
                hideKeyboard()
                if (!Validator.isValidUserName(etUsername, true, this@AdminAddStaffActivity)) {
                    return@setOnClickListener
                } else if (etCnic.text!!.isEmpty()) {
                    etCnic.error = getString(R.string.str_invalid_cnic)
                    return@setOnClickListener
                } else if (!Validator.isValidEmail(etEmail, true, this@AdminAddStaffActivity)) {
                    return@setOnClickListener
                } else if (!Validator.isValidPhone(etPhone, true, this@AdminAddStaffActivity)) {
                    return@setOnClickListener
                } else if (etReferralCode.text.isNullOrEmpty()) {
                    etReferralCode.error = getString(R.string.str_invalid_refral_code)
                    return@setOnClickListener
                } else if (!Validator.isValidAddress(
                        etPaypalAddress,
                        true,
                        this@AdminAddStaffActivity
                    )
                ) {
                    return@setOnClickListener
                } else {
                    binding.progressLayout.isVisible = true
                    saveProfile()
                }
            }
        }
    }

    private fun saveProfile() {
        if (imagePath != null) {
            uploadImageToStorage(imagePath!!)
        } else {
            createProfile("")
        }
    }

    private fun uploadImageToStorage(imagePath: String){
        val file = File(imagePath)
        val uniqueId = UUID.randomUUID().toString()
        val storagePath = "Staff Pictures/$uniqueId"
        uploadFile(file, storagePath)
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
                        createProfile(uri.toString())
                    }
                }
            }
        }.addOnProgressListener { taskSnapshot ->

        }.addOnFailureListener { e ->
            binding.progressLayout.isVisible = false
            showMessage(findViewById(android.R.id.content),e.message.toString())
        }
    }

    private fun createProfile(image:String){
        val userId = staffReference.document()
        val userName = binding.etUsername.text.toString().trim()
        val cnic = binding.etCnic.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val referralCode = binding.etReferralCode.text.toString().trim()
        val address = binding.etPaypalAddress.text.toString().trim()

        val user = Admin(
            id = userId.id, userName = userName, email = email,
            referralCode = referralCode,cnic = cnic, phone = phone, address = address, profileImage = image
        )
        updateProfile(user)
    }

    private fun updateProfile(user: Admin) {
        val ref = staffReference.document(user.id!!)
        ref.set(user).addOnSuccessListener {
            binding.progressLayout.visibility = View.GONE
            showMessage(findViewById(android.R.id.content),"Save successfully")
            finish()
        }.addOnFailureListener{
            binding.progressLayout.visibility = View.GONE
            showMessage(findViewById(android.R.id.content),it.message.toString())
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
            .into(binding.civProfilePic)
    }

    private fun initView() {
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        staffReference = db.collection(Constants.STAFF_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()
    }
}