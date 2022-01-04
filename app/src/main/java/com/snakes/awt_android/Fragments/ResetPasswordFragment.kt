package com.snakes.awt_android.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentResetPasswordBinding
import com.snakes.awt_android.databinding.FragmentSignupBinding

class RsetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPasswordBinding.inflate(layoutInflater)
        setClickListener()
        return binding.root
    }

    private fun setClickListener() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            tvLogin.setOnClickListener {
                findNavController().navigate(R.id.signIn_Fragment)
            }
        }
    }


}