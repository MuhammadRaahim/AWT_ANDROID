package com.snakes.awt_android.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.snakes.awt_android.Activities.WebActivity
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        setClickListener()
        return binding.root
    }

    private fun setClickListener() {
        binding.apply {
            btnSetProfile.setOnClickListener {
                findNavController().navigate(R.id.signup_Fragment)
            }
            tvAlreadyHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.signIn_Fragment)
            }
            tvSignIn.setOnClickListener {
                findNavController().navigate(R.id.signIn_Fragment)
            }
            btnGoToWebsite.setOnClickListener {
                startActivity(Intent(requireActivity(),WebActivity::class.java))
            }
        }
    }

}