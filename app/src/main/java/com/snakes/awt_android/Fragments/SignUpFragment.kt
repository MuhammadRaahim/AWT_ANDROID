package com.snakes.awt_android.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.horizam.skbhub.Utils.Constants.Companion.USERS_DATABASE_ROOT
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.Models.User
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.hideKeyboard
import com.snakes.awt_android.Utils.Validator
import com.snakes.awt_android.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var genericHandler: GenericHandler
    private lateinit var userReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        initViews()
        setClickListener()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun initViews() {
        auth = Firebase.auth
        db = Firebase.firestore
        userReference = db.collection(USERS_DATABASE_ROOT)
        firebaseStorage = FirebaseStorage.getInstance()
    }

    private fun setClickListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvAlreadyHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.signIn_Fragment)
            }
            tvSignIn.setOnClickListener {
                findNavController().navigate(R.id.signIn_Fragment)
            }
            btnSignUp.setOnClickListener {
                hideKeyboard()
                if (!Validator.isValidUserName(etFullName, true,requireContext())) {
                    return@setOnClickListener
                }else if (!Validator.isValidEmail(etEmail, true, requireContext())) {
                    return@setOnClickListener
                } else if (!Validator.isValidPhone(etCarrierNumber, true, requireContext())) {
                    return@setOnClickListener
                } else if (!Validator.isValidAddress(etAddress, true, requireContext())) {
                    return@setOnClickListener
                }  else if (etReferral.text.isNullOrEmpty()) {
                    etReferral.error = getString(R.string.str_invalid_refral_code)
                    return@setOnClickListener
                } else if (!Validator.isValidPassword(etPassword, true, requireContext())){
                    return@setOnClickListener
                } else {
                    genericHandler.showProgressBar(true)
                    signUp()
                }
            }
        }
    }

    private fun signUp() {
        auth.createUserWithEmailAndPassword(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    getData(task.result.user!!.uid)
                } else {
                    genericHandler.showProgressBar(false)
                    genericHandler.showMessage(task.exception!!.message.toString())
                }
            }
    }

    private fun getData(uid: String) {
        val userName = binding.etFullName.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val phone = binding.ccp.selectedCountryCodeWithPlus+binding.etCarrierNumber.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val referralCode = binding.etReferral.text.toString().trim()

        val user = User(
            id = uid, userName = userName, email = email,
            phone = phone, address = address, referralCode = referralCode
        )

        createProfile(user)
    }

    private fun createProfile(user: User) {
        val ref = userReference.document(user.id!!)
        ref.set(user).addOnSuccessListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(getString(R.string.str_signup_successfully))
            findNavController().navigate(R.id.welcome_Fragment)
        }.addOnFailureListener{
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message.toString())
        }
    }

}