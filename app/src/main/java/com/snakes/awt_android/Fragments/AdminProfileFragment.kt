package com.snakes.awt_android.Fragments

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.Models.Admin
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.hideKeyboard
import com.snakes.awt_android.Utils.ImageFilePath
import com.snakes.awt_android.Utils.Validator
import com.snakes.awt_android.databinding.DialogFileUploadingBinding
import com.snakes.awt_android.databinding.FragmentAdminProfileBinding
import java.io.File

class AdminProfileFragment : Fragment() {

    private lateinit var binding: FragmentAdminProfileBinding
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var adminReference: CollectionReference
    private lateinit var genericHandler: GenericHandler
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var dialogFileUpload: Dialog
    private lateinit var bindingFileUploadDialog: DialogFileUploadingBinding
    private var imagePath: String? = null
    private var image: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminProfileBinding.inflate(layoutInflater)
        initView()
        setClickListeners()
        getUserFirebaseData()
        return binding.root
    }

    private fun getUserFirebaseData(){
        genericHandler.showProgressBar(true)
        currentFirebaseUser = auth.currentUser!!
        adminReference.document(currentFirebaseUser!!.uid)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    val document: DocumentSnapshot = it.result
                    if (document.exists()) {
                        val user = document.toObject(Admin::class.java)
                        setData(user)
                    }
                    genericHandler.showProgressBar(false)
                }else{
                    genericHandler.showProgressBar(false)
                    genericHandler.showMessage(it.exception.toString())
                }

            }
    }

    private fun setData(user: Admin?) {
        if (user!!.profileImage != null){
            image = user!!.profileImage!!
            Glide.with(requireContext()).load(user.profileImage).placeholder(R.drawable.img_profile_cover_placeholder)
                .into(binding.civProfilePic)
        }
        binding.apply {
            etUsername.setText(user!!.userName)
            etEmail.setText(user.email)
            etPhone.setText(user.phone)
            etCnic.setText(user.cnic)
            etReferralCode.setText(user.referralCode)
            etPaypalAddress.setText(user.address)

        }
        genericHandler.showProgressBar(false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun initFileUploadDialog() {
        dialogFileUpload = Dialog(requireContext())
        bindingFileUploadDialog = DialogFileUploadingBinding.inflate(layoutInflater)
        dialogFileUpload.setContentView(bindingFileUploadDialog.root)
        setDialogWidth()
    }

    private fun setDialogWidth() {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialogFileUpload.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialogFileUpload.window!!.attributes = layoutParams
    }

    private fun initView() {
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        auth = FirebaseAuth.getInstance()
        adminReference = db.collection(Constants.Admin_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()
        initFileUploadDialog()
    }

    private fun setClickListeners() {
        binding.apply {
            icBack.setOnClickListener {
                findNavController().popBackStack()
            }
            ivChangeProfile.setOnClickListener {
                launchGalleryIntent()
            }
            btnSaveProfile.setOnClickListener {
                hideKeyboard()
                if (!Validator.isValidUserName(etUsername, true,requireContext())) {
                    return@setOnClickListener
                }else if (etCnic.text!!.isEmpty()) {
                    etCnic.error = getString(R.string.str_invalid_cnic)
                    return@setOnClickListener
                }else if (!Validator.isValidEmail(etEmail, true, requireContext())) {
                    return@setOnClickListener
                } else if (!Validator.isValidPhone(etPhone, true, requireContext())) {
                    return@setOnClickListener
                } else if (etReferralCode.text.isNullOrEmpty()) {
                    etReferralCode.error = getString(R.string.str_invalid_refral_code)
                    return@setOnClickListener
                } else if (!Validator.isValidAddress(etPaypalAddress, true, requireContext())) {
                    return@setOnClickListener
                }else {
                    genericHandler.showProgressBar(true)
                    saveProfile()
                }
            }
        }
    }

    private fun saveProfile() {
        if (imagePath != null) {
            uploadImageToStorage(imagePath!!)
        } else {
            createProfile(image!!)
        }
    }

    private fun uploadImageToStorage(imagePath: String){
        val file = File(imagePath)
//        val uniqueId = UUID.randomUUID().toString()
        val storagePath = "Admin Profile/Admin Picture"
        uploadFile(file, storagePath)
    }

    private fun uploadFile(file: File, storagePath: String) {
        val ext: String = file.extension
        if (ext.isEmpty()) {
            genericHandler.showMessage("Something went wrong")
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
                        if (dialogFileUpload.isShowing) {
                            dialogFileUpload.dismiss()
                        }
                        createProfile(uri.toString())
                    }
                }
            }
        }.addOnProgressListener { taskSnapshot ->
            val progress: Double =
                100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
            if (!dialogFileUpload.isShowing) {
                dialogFileUpload.show()
            }
            bindingFileUploadDialog.progressBar.progress = progress.toInt()
            bindingFileUploadDialog.tvFileProgress.text = progress.toInt().toString().plus("%")
        }.addOnFailureListener { e ->
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(e.message.toString())
            if (dialogFileUpload.isShowing) {
                dialogFileUpload.dismiss()
            }
        }
    }

    private fun createProfile(image:String){
        val userId = currentFirebaseUser.uid
        val userName = binding.etUsername.text.toString().trim()
        val cnic = binding.etCnic.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val referralCode = binding.etReferralCode.text.toString().trim()
        val address = binding.etPaypalAddress.text.toString().trim()

        val user = Admin(
            id = userId, userName = userName, email = email,
            referralCode = referralCode,cnic = cnic, phone = phone, address = address, profileImage = image
        )
        updateProfile(user)
    }

    private fun updateProfile(user: Admin) {
        val ref = adminReference.document(currentFirebaseUser.uid)
        ref.set(user).addOnSuccessListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage("Save successfully")
        }.addOnFailureListener{
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message.toString())
        }
    }

    private fun launchGalleryIntent() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                getImageFromGallery.launch("image/*")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
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
                Toast.makeText(requireContext(),getString(R.string.permission_required)
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
                action(requireView().findViewById(android.R.id.content))
            }.show()
        } else {
            snackBar.show()
        }
    }

    private fun setPicture(uri: Uri?) {
        imagePath = ImageFilePath().getFilePath(uri!!,requireContext())
        Glide.with(this).load(uri).placeholder(R.drawable.img_profile_cover_placeholder)
            .into(binding.civProfilePic)
    }

}