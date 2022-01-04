package com.snakes.awt_android.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        setClickListener()
        return binding.root
    }

    private fun setClickListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSignUp.setOnClickListener {
                findNavController().navigate(R.id.welcome_Fragment)
            }
            tvAlreadyHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.signIn_Fragment)
            }
            tvSignIn.setOnClickListener {
                findNavController().navigate(R.id.signIn_Fragment)
            }
        }
    }

}