package com.snakes.awt_android.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.Adapters.AdminDasterKhawanAdapter
import com.snakes.awt_android.Adapters.AdminSchoolAdapter
import com.snakes.awt_android.Adapters.SchoolKhanaAdapter
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.Models.SchoolKhana
import com.snakes.awt_android.Utils.BaseUtils
import com.snakes.awt_android.databinding.ActivityAdminSchoolBinding
import java.lang.Exception

class AdminSchoolActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminSchoolBinding
    private lateinit var schoolReference: CollectionReference
    private lateinit var adapter: AdminSchoolAdapter
    private lateinit var schoolList: ArrayList<SchoolKhana>
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSchoolBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setSchoolRecyclerView()
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        getSchoolKhawan()
    }

    private fun setClickListeners() {
        binding.fabAddService.setOnClickListener {
            startActivity(Intent(this,AdminAddSchoolActivity::class.java))
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        schoolList = ArrayList()
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        schoolReference = db.collection(Constants.SCHOOL_DATABASE_ROOT)
    }

    private fun setSchoolRecyclerView() {
        binding.rvSchool.layoutManager =  GridLayoutManager(this,2,  RecyclerView.VERTICAL, false)
        adapter = AdminSchoolAdapter(schoolList)
        binding.rvSchool.adapter = adapter
    }

    private fun getSchoolKhawan() {
        schoolList.clear()
        binding.progressLayout.isVisible = true
        schoolReference.get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val service = documentSnapshot.toObject(SchoolKhana::class.java)
                try {
                    schoolList.add(service)

                } catch (ex: Exception) {
                    binding.progressLayout.isVisible = false
                    BaseUtils.showMessage(findViewById(android.R.id.content), ex.message.toString())
                }
            }
            if (schoolList.isNotEmpty()) {
                adapter.updateList(schoolList)
                binding.rvSchool.isVisible = true
                binding.tvNoData.isVisible = false
            } else {
                binding.rvSchool.isVisible = false
                binding.tvNoData.isVisible = true
            }
            binding.progressLayout.isVisible = false

        }.addOnFailureListener {
            binding.progressLayout.isVisible = false
            BaseUtils.showMessage(findViewById(android.R.id.content), it.message.toString())
        }
    }
}