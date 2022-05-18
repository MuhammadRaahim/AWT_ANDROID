package com.snakes.awt_android.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.Constants.Companion.Admin_DATABASE_ROOT
import com.horizam.skbhub.Utils.Constants.Companion.PHONE_NUMBER
import com.horizam.skbhub.Utils.PrefManager
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentAdminOtpBinding
import java.util.concurrent.TimeUnit


class AdminOtpFragment : Fragment() {

    private lateinit var binding: FragmentAdminOtpBinding
    private lateinit var phone: String
    private lateinit var auth: FirebaseAuth
    private lateinit var genericHandler: GenericHandler
    private lateinit var adminReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var manager: PrefManager
    private var currentFirebaseUser: FirebaseUser? = null
    private var  codeVerification: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminOtpBinding.inflate(layoutInflater)

        setUpUI()
        setClickListeners()
        setVerification()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun setUpUI() {
        phone = arguments?.getString(PHONE_NUMBER).toString()
        binding.tvFourDigit.text = getString(R.string.str_enter_the_4_digit_code_sent_to_you_at_0323_4004748).plus("\n").plus(phone)
        db = Firebase.firestore
        manager = PrefManager(requireContext())
        adminReference = db.collection(Admin_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
    }

    private fun setVerification() {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        auth.setLanguageCode("en")

    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            var code = credential.smsCode
            if (code != null) {
                binding.otpView.setOTP(code)
                verifyCode(code)
            }
        }
        override fun onVerificationFailed(e: FirebaseException) {
            if (e is FirebaseAuthInvalidCredentialsException) {
                genericHandler.showMessage(e.message!!)
            } else if (e is FirebaseTooManyRequestsException) {
                genericHandler.showMessage(e.message!!)
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            codeVerification = verificationId
        }
    }

    private fun verifyCode(code: String) {
        if (!codeVerification.isNullOrEmpty()) {
            val credential = PhoneAuthProvider.getCredential(codeVerification!!, code)
            signInWithCredential(credential)
        }else{
            genericHandler.showProgressBar(false)
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    manager.session = Constants.ADMIN
                    checkForUser()
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        genericHandler.showProgressBar(false)
                        genericHandler.showMessage(task.exception.toString())
                    }
                }
            }
    }

    private fun setClickListeners() {
        binding.apply {
            btnVerify.setOnClickListener {
                validateOTP()
            }
            tvResend.setOnClickListener {
                setVerification()
            }
        }
    }

    private fun validateOTP() {
        val otpCode = binding.otpView.otp
        if (otpCode!!.length<6) {
            binding.otpView.showError()
        }else{
            genericHandler.showProgressBar(true)
            verifyCode(otpCode)
        }
    }

    private fun checkForUser(){
        currentFirebaseUser = auth.currentUser!!
        adminReference.document(currentFirebaseUser!!.uid)
            .get().addOnCompleteListener {
                if (it.isSuccessful){
                    genericHandler.showProgressBar(false)
                    val document: DocumentSnapshot = it.result
                    if (document.exists()) {
                        findNavController().navigate(R.id.navigation_admin_dashboard)
                    } else {
                        val bundle = bundleOf(PHONE_NUMBER to phone)
                        findNavController().navigate(R.id.navigation_admin_signup, bundle)
                    }
                }else{
                    genericHandler.showProgressBar(false)
                    genericHandler.showMessage(it.exception.toString())
                }

            }
    }

}