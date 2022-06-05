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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.Constants.Companion.DASTERKHAWAN_DATABASE_ROOT
import com.horizam.skbhub.Utils.Constants.Companion.SCHOOL_DATABASE_ROOT
import com.horizam.skbhub.Utils.Constants.Companion.SERVICE_DATABASE_ROOT
import com.jdars.shared_online_business.CallBacks.DrawerHandler
import com.snakes.awt_android.Adapters.DasterKhawanAdapter
import com.snakes.awt_android.Adapters.SchoolKhanaAdapter
import com.snakes.awt_android.Adapters.ServicesAdapter
import com.snakes.awt_android.CallBacks.GenericHandler
import com.snakes.awt_android.CallBacks.OnItemClickListener
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.Models.SchoolKhana
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.DasterkhawanItemBinding
import com.snakes.awt_android.databinding.FragmentHomeBinding
import java.lang.Exception


class HomeFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var drawerHandlerCallback: DrawerHandler
    private lateinit var genericHandler: GenericHandler

    private lateinit var adapterDasterkhawan: DasterKhawanAdapter
    private lateinit var servicesAdapter: ServicesAdapter
    private lateinit var schoolKhanaAdapter: SchoolKhanaAdapter

    private lateinit var dasterKhawanReference: CollectionReference
    private lateinit var schoolKhanaReference: CollectionReference
    private lateinit var servicesReference: CollectionReference


    private lateinit var servicesList: ArrayList<Service>
    private lateinit var dasterKhawanList: ArrayList<DasterKhawan>
    private lateinit var schoolList: ArrayList<SchoolKhana>

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser

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
        dasterKhawanList = ArrayList()
        schoolList = ArrayList()
        setUpFireBase()
        getData()

        binding.swipeRefreshLayout.setOnRefreshListener{
            getData()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getData() {
        genericHandler.showProgressBar(true)
        dasterKhawanList.clear()
        dasterKhawanReference.get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val dasterKhawan  = documentSnapshot.toObject(DasterKhawan::class.java)
                try {
                    dasterKhawanList.add(dasterKhawan)
                } catch (ex: Exception) {
                    genericHandler.showProgressBar(true)
                    genericHandler.showMessage(ex.message.toString())
                }
            }
            if (dasterKhawanList.isNotEmpty()){
                adapterDasterkhawan.updateList(dasterKhawanList)
            }else{
                genericHandler.showProgressBar(false)
            }
            getSchools()

        }.addOnFailureListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message!!)
        }

    }

    private fun getSchools() {
        schoolList.clear()
        schoolKhanaReference.get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val school  = documentSnapshot.toObject(SchoolKhana::class.java)
                try {
                    schoolList.add(school)
                } catch (ex: Exception) {
                    genericHandler.showProgressBar(true)
                    genericHandler.showMessage(ex.message.toString())
                }
            }
            if (schoolList.isNotEmpty()){
                schoolKhanaAdapter.updateList(schoolList)
            }else{
                genericHandler.showProgressBar(false)
            }
            getServices()
        }.addOnFailureListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message!!)
        }
    }

    private fun getServices() {
        servicesList.clear()
        servicesReference.get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val service  = documentSnapshot.toObject(Service::class.java)
                try {
                    servicesList.add(service)
                } catch (ex: Exception) {
                    genericHandler.showProgressBar(true)
                    genericHandler.showMessage(ex.message.toString())
                }
            }
            if (servicesList.isNotEmpty()){
                servicesAdapter.updateList(servicesList)
            }else{
                genericHandler.showProgressBar(false)
            }

        }.addOnFailureListener {
            genericHandler.showProgressBar(false)
            genericHandler.showMessage(it.message!!)
        }
    }

    private fun setUpFireBase() {
        db = Firebase.firestore
        dasterKhawanReference = db.collection(DASTERKHAWAN_DATABASE_ROOT)
        schoolKhanaReference = db.collection(SCHOOL_DATABASE_ROOT)
        servicesReference = db.collection(SERVICE_DATABASE_ROOT)
        auth = FirebaseAuth.getInstance()
        currentFirebaseUser = auth.currentUser!!
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        drawerHandlerCallback = context as DrawerHandler
        genericHandler = context as GenericHandler
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
        adapterDasterkhawan = DasterKhawanAdapter(dasterKhawanList,this)
        binding.rvDasterkawan.adapter = adapterDasterkhawan
    }

    private fun setSchoolKhawanRecyclerView() {
        binding.rvSchoolKhana.layoutManager =  LinearLayoutManager(requireActivity(),  RecyclerView.HORIZONTAL, false)
        schoolKhanaAdapter = SchoolKhanaAdapter(schoolList)
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