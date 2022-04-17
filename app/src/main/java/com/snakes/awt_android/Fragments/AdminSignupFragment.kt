package com.snakes.awt_android.Fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentAdminSignupBinding


class AdminSignupFragment : Fragment() {

    private lateinit var binding: FragmentAdminSignupBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminSignupBinding.inflate(layoutInflater)

        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            btnSaveProfile.setOnClickListener {
                findNavController().navigate(R.id.navigation_admin_dashboard)
            }
        }
    }

}