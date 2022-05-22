package com.snakes.awt_android.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.jdars.shared_online_business.CallBacks.DrawerHandler
import com.snakes.awt_android.Adapters.DasterKhawanAdapter
import com.snakes.awt_android.Adapters.SchoolKhanaAdapter
import com.snakes.awt_android.Adapters.ServicesAdapter
import com.snakes.awt_android.CallBacks.OnItemClickListener
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.DasterkhawanItemBinding
import com.snakes.awt_android.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterDasterkhawan: DasterKhawanAdapter
    private lateinit var servicesAdapter: ServicesAdapter
    private lateinit var schoolKhanaAdapter: SchoolKhanaAdapter
    private lateinit var drawerHandlerCallback: DrawerHandler

    private lateinit var servicesList: ArrayList<Service>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        initViews()
        setClickListener()
        setImageSlider()
        setDasterKhawanRecyclerView()
        setSchoolKhawanRecyclerView()
        setServicesRecyclerView()
        return binding.root
    }

    private fun initViews() {
        servicesList = ArrayList()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        drawerHandlerCallback = context as DrawerHandler
    }

    private fun setClickListener() {
        binding.apply {
            toolbar.ivGraph.setOnClickListener {
                findNavController().navigate(R.id.navigation_graph)
            }
            toolbar.ivToolbar.setOnClickListener {
                drawerHandlerCallback.openDrawer()
            }

        }
    }

    private fun setServicesRecyclerView() {
        binding.rvService.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.VERTICAL, false)
        servicesAdapter = ServicesAdapter(servicesList)
        binding.rvService.adapter = servicesAdapter
    }

    private fun setDasterKhawanRecyclerView() {
        binding.rvDasterkawan.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.HORIZONTAL, false)
        adapterDasterkhawan = DasterKhawanAdapter(this)
        binding.rvDasterkawan.adapter = adapterDasterkhawan
    }

    private fun setSchoolKhawanRecyclerView() {
        binding.rvSchoolKhana.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.HORIZONTAL, false)
        schoolKhanaAdapter = SchoolKhanaAdapter()
        binding.rvSchoolKhana.adapter = schoolKhanaAdapter
    }

    private fun setImageSlider() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.img_slider_1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img_slider_2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.img_slider_3, ScaleTypes.FIT))
        binding.imageSlider.setImageList(imageList)
    }

    override fun onItemClick() {
        findNavController().navigate(R.id.navigation_maps)
    }


}