package com.snakes.awt_android.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.PrefManager
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.Models.SchoolKhana
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils
import com.snakes.awt_android.databinding.ActivityAdminSchoolDeatailsBinding

class AdminSchoolDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminSchoolDeatailsBinding
    private lateinit var schoolKhana: SchoolKhana
    private lateinit var manager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSchoolDeatailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()

    }

    private fun initViews() {
        manager = PrefManager(this)
        schoolKhana = intent.extras!!.get(Constants.SCHOOLKHANA_OBJECT) as SchoolKhana
        setData()
    }

    private fun setData() {
        binding.apply {
            Glide.with(this@AdminSchoolDetailsActivity).load(schoolKhana.photo)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(ivEvent)
            tvEventName.text = schoolKhana.name
            tvDescription.text = schoolKhana.description
            tvDetails.text = schoolKhana.details
            duration.text = "${schoolKhana.startTime} pm to ${schoolKhana.endTime} pm"
            location.text = schoolKhana.locatoion
        }
    }

    private fun setClickListeners() {
        binding.apply {
            ivBadge.setOnClickListener {
                BaseUtils.loadWebView(
                    this@AdminSchoolDetailsActivity,
                    "https://allahwalaytrust.org.pk/our-projects/"
                )
            }
            ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}