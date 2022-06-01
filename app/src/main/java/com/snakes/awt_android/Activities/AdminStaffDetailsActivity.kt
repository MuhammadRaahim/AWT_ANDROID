package com.snakes.awt_android.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.Models.Admin
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.ActivityAdminStaffDeatailsBinding

class AdminStaffDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminStaffDeatailsBinding
    private lateinit var user: Admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStaffDeatailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        user = intent.extras!!.get(Constants.STAFF) as Admin
        binding.apply {
            Glide.with(this@AdminStaffDetailsActivity).load(user.profileImage)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(binding.civProfilePic)
            etUsername.setText(user.userName)
            etCnic.setText(user.cnic)
            etEmail.setText(user.email)
            etPhone.setText(user.phone)
            etReferralCode.setText(user.referralCode)
            etPaypalAddress.setText(user.address)
        }
    }
}