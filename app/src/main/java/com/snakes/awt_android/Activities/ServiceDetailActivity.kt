package com.snakes.awt_android.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants.Companion.ADMIN
import com.horizam.skbhub.Utils.Constants.Companion.SERVICE_OBJECT
import com.horizam.skbhub.Utils.PrefManager
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.loadWebView
import com.snakes.awt_android.databinding.ActivityServiceDetailBinding

class ServiceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityServiceDetailBinding
    private lateinit var service: Service
    private lateinit var manager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()


    }

    private fun setClickListeners() {
        binding.apply {
            ivBadge.setOnClickListener {
                loadWebView(this@ServiceDetailActivity,"https://allahwalaytrust.org.pk/our-projects/")
            }
            ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initViews() {
        manager = PrefManager(this)
        service = intent.extras!!.get(SERVICE_OBJECT) as Service
        setData()
    }

    private fun setData() {
        binding.apply {
            Glide.with(this@ServiceDetailActivity).load(service.serviceImage)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(ivEvent)
            tvEventName.text = service.serviceName
            tvDescription.text = service.description
            tvDetails.text = service.details

            if (manager.session == ADMIN){
                btnDonate.isVisible = false
                tvDonation.isVisible = true
                donation.isVisible = true
            }else{
                btnDonate.isVisible = true
                tvDonation.isVisible = false
                donation.isVisible = false
            }
        }
    }
}