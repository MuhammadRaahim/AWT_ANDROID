package com.snakes.awt_android.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.PrefManager
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils
import com.snakes.awt_android.databinding.ActivityDasterKhawanDetailsBinding

class DasterKhawanDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDasterKhawanDetailsBinding
    private lateinit var dasterKhawan: DasterKhawan
    private lateinit var manager: PrefManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDasterKhawanDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initViews()
        setClickListeners()

    }

    private fun setClickListeners() {
        binding.apply {
            ivBadge.setOnClickListener {
                BaseUtils.loadWebView(
                    this@DasterKhawanDetailsActivity,
                    "https://allahwalaytrust.org.pk/our-projects/"
                )
            }
            ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun initViews() {
        manager = PrefManager(this)
        dasterKhawan = intent.extras!!.get(Constants.DASTERKHAWAN_OBJECT) as DasterKhawan
        setData()
    }

    private fun setData() {
        binding.apply {
            Glide.with(this@DasterKhawanDetailsActivity).load(dasterKhawan.photo)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(ivEvent)
            tvEventName.text = dasterKhawan.name
            tvDescription.text = dasterKhawan.description
            tvDetails.text = dasterKhawan.details
            duration.text = "${dasterKhawan.startTime} pm to ${dasterKhawan.endTime} pm"
            date.text = dasterKhawan.date
            location.text = dasterKhawan.locatoion
        }
    }
}