package com.snakes.awt_android.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.snakes.awt_android.MainActivity
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentSigninBinding


class SigninFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(layoutInflater)
        setClickListener()
        return binding.root
    }
    private fun setClickListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnLogin.setOnClickListener {
                startActivity(Intent(requireActivity(),MainActivity::class.java))
            }
            tvForgetPassword.setOnClickListener {
                findNavController().navigate(R.id.reset_password_Fragment)
            }
            tvSignUp.setOnClickListener {
                findNavController().navigate(R.id.signup_Fragment)
            }
        }
    }

}