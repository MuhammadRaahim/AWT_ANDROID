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
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.Adapters.AdminDasterKhawanAdapter
import com.snakes.awt_android.Adapters.ServicesAdapter
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils
import com.snakes.awt_android.databinding.ActivityAdminDasterKhawanBinding
import java.lang.Exception

class AdminDasterKhawanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDasterKhawanBinding
    private lateinit var adapter: AdminDasterKhawanAdapter
    private lateinit var daterKhawanList: ArrayList<DasterKhawan>
    private lateinit var dasterKhawanReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var currentFirebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDasterKhawanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()
        setServicesRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getDasterKhawan()
    }

    private fun initViews() {
        daterKhawanList = ArrayList()
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        dasterKhawanReference = db.collection(Constants.DASTERKHAWAN_DATABASE_ROOT)
    }

    private fun setServicesRecyclerView() {
        binding.rvServices.layoutManager =  LinearLayoutManager(this,  RecyclerView.VERTICAL, false)
        adapter = AdminDasterKhawanAdapter(daterKhawanList)
        binding.rvServices.adapter = adapter
    }

    private fun setClickListeners() {
        binding.fabAddService.setOnClickListener {
            startActivity(Intent(this,AddDasterKhawanActivity::class.java))
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getDasterKhawan() {
        daterKhawanList.clear()
        binding.progressLayout.isVisible = true
        dasterKhawanReference.get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val service  = documentSnapshot.toObject(DasterKhawan::class.java)
                try {
                    daterKhawanList.add(service)

                } catch (ex: Exception) {
                    binding.progressLayout.isVisible = false
                    BaseUtils.showMessage(findViewById(android.R.id.content), ex.message.toString())
                }
            }
            if (daterKhawanList.isNotEmpty()){
                adapter.updateList(daterKhawanList)
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
            BaseUtils.showMessage(findViewById(android.R.id.content), it.message.toString())
        }


    }
}