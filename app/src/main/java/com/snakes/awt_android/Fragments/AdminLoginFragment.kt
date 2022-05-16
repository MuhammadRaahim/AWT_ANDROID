package com.snakes.awt_android.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.horizam.skbhub.Utils.Constants.Companion.PHONE_NUMBER
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.Validator.Companion.checkPhone
import com.snakes.awt_android.databinding.FragmentAdminLoginBinding


class AdminLoginFragment : Fragment() {

    private lateinit var binding: FragmentAdminLoginBinding
    private lateinit var genericHandler: GenericHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminLoginBinding.inflate(layoutInflater)

        setClickListeners()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        genericHandler = context as GenericHandler
    }

    private fun setClickListeners() {
        binding.apply {
            btnLogIn.setOnClickListener {
                validatePhoneNumber()
            }
        }
    }

    private fun validatePhoneNumber() {
        val phone = binding.ccp.selectedCountryCodeWithPlus+binding.etPhone.text.toString().trim()
        val valid = checkPhone(phone)
        when {
            valid -> {
                val bundle = bundleOf(PHONE_NUMBER to phone)
                findNavController().navigate(R.id.navigation_admin_otp, bundle)
            }
            else -> genericHandler.showMessage(getString(R.string.str_enter_valid_phone))
        }
    }

}