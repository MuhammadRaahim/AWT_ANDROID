package com.snakes.awt_android.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentAdminLoginBinding


class AdminLoginFragment : Fragment() {

    private lateinit var binding: FragmentAdminLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminLoginBinding.inflate(layoutInflater)

        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            btnLogIn.setOnClickListener {
                findNavController().navigate(R.id.navigation_admin_signup)
            }
        }
    }

}