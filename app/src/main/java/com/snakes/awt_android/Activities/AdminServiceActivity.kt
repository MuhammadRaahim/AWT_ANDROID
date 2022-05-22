package com.snakes.awt_android.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.Adapters.ServicesAdapter
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.Utils.BaseUtils.Companion.showMessage
import com.snakes.awt_android.databinding.ActivityAdminServiceBinding
import java.lang.Exception

class AdminServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminServiceBinding
    private lateinit var servicesAdapter: ServicesAdapter
    private lateinit var serviceList: ArrayList<Service>
    private lateinit var serviceReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()
        setServicesRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getServices()
    }

    private fun initViews() {
        serviceList = ArrayList()
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        serviceReference = db.collection(Constants.SERVICE_DATABASE_ROOT)
    }

    private fun setClickListeners() {
        binding.fabAddService.setOnClickListener {
            startActivity(Intent(this,CreateServiceActivity::class.java))
        }
    }

    private fun setServicesRecyclerView() {
        binding.rvServices.layoutManager =  LinearLayoutManager(this,  RecyclerView.VERTICAL, false)
        servicesAdapter = ServicesAdapter(serviceList)
        binding.rvServices.adapter = servicesAdapter
    }

    private fun getServices() {
        serviceList.clear()
        binding.progressLayout.isVisible = true
        serviceReference.get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val service  = documentSnapshot.toObject(Service::class.java)
                try {
                   serviceList.add(service)

                } catch (ex: Exception) {
                    binding.progressLayout.isVisible = false
                    showMessage(findViewById(android.R.id.content),ex.message.toString())
                }
            }
            if (serviceList.isNotEmpty()){
                servicesAdapter.updateList(serviceList)
                binding.rvServices.isVisible = true
                binding.tvNoData.isVisible = false
            }
            else{
                binding.rvServices.isVisible = false
                binding.tvNoData.isVisible = true
            }
            binding.progressLayout.isVisible = false

        }.addOnFailureListener {
            binding.progressLayout.isVisible = false
            showMessage(findViewById(android.R.id.content),it.message.toString())
        }


    }
}