package com.snakes.awt_android.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentDasterKhawanBottomBinding
import com.snakes.awt_android.events.DirectionEvent
import org.greenrobot.eventbus.EventBus


class DasterKhawanBottomFragment(
    var daster: DasterKhawan
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDasterKhawanBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDasterKhawanBottomBinding.inflate(layoutInflater)

        initViews()
        setClickListeners()


        return binding.root
    }


    private fun setClickListeners() {
        binding.btnDirection.setOnClickListener{
            EventBus.getDefault().post(DirectionEvent())
            dismiss()
        }
    }

    private fun initViews() {
        setData()
    }

    private fun setData() {
        binding.apply {
            Glide.with(requireContext()).load(daster.photo)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(ivImage)
            tvTitle.text = daster.name
            tvLocation.text = daster.locatoion
            tvDate.text = daster.date
            tvTime.text = "${daster.startTime} pm to ${daster.endTime}pm"
            tvDescription.text = daster.description
        }
    }

}