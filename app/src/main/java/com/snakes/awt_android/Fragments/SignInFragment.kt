package com.snakes.awt_android.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.MainActivity
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.hideKeyboard
import com.snakes.awt_android.databinding.FragmentSigninBinding


class SigninFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    private lateinit var genericHandler: GenericHandler
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(layoutInflater)
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
    }

    private fun setClickListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvForgetPassword.setOnClickListener {
                findNavController().navigate(R.id.reset_password_Fragment)
            }
            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.signup_Fragment)
            }
            btnLogin.setOnClickListener {
                hideKeyboard()
                if(!binding.etEmail.text.isNullOrEmpty() && !binding.etPassword.text.isNullOrEmpty()){
                    genericHandler.showProgressBar(true)
                    loginUser()
                }else{
                    genericHandler.showMessage("Invalid credentials")
                }
            }
        }
    }

    private fun loginUser() {
        auth.signInWithEmailAndPassword(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString().trim())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    genericHandler.showProgressBar(false)
                    var intent = Intent(requireActivity(), MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    genericHandler.showProgressBar(false)
                    genericHandler.showMessage(task.exception!!.message.toString())
                }
            }
    }

}