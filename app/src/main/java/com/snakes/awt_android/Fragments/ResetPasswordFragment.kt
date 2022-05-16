package com.snakes.awt_android.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.hideKeyboard
import com.snakes.awt_android.Utils.Validator.Companion.isValidEmail
import com.snakes.awt_android.databinding.FragmentResetPasswordBinding
import com.snakes.awt_android.databinding.FragmentSignupBinding

class RsetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding
    private lateinit var genericHandler: GenericHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)
        setClickListener()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun setClickListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvLogin.setOnClickListener {
                findNavController().navigate(R.id.signIn_Fragment)
            }
            btnResetPassword.setOnClickListener {
                hideKeyboard()
                if (!isValidEmail(etEmail, true, requireContext())){
                    return@setOnClickListener
                }else{
                    setForgetPasswordEmail()
                }
            }
        }
    }

    private fun setForgetPasswordEmail() {
        genericHandler.showProgressBar(true)
        FirebaseAuth.getInstance().sendPasswordResetEmail(binding.etEmail.text.toString().trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    genericHandler.showProgressBar(false)
                    genericHandler.showMessage("Email Send Successfully")
                    findNavController().popBackStack()
                }else{
                    genericHandler.showProgressBar(false)
                    genericHandler.showMessage(task.exception.toString())
                }
            }
    }


}