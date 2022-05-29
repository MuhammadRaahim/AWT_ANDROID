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
import com.snakes.awt_android.Adapters.AdminStaffAdapter
import com.snakes.awt_android.Models.Admin
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.Models.User
import com.snakes.awt_android.Utils.BaseUtils
import com.snakes.awt_android.databinding.ActivityAdminStaffBinding
import java.lang.Exception

class AdminStaffActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminStaffBinding
    private lateinit var adapter: AdminStaffAdapter
    private lateinit var staffList: ArrayList<Admin>
    private lateinit var staffReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var currentFirebaseUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStaffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()
        setServicesRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getStaff()
    }

    private fun setClickListeners() {
        binding.fabAddService.setOnClickListener {
            startActivity(Intent(this,AdminAddStaffActivity::class.java))
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setServicesRecyclerView() {
        binding.rvStaff.layoutManager =  LinearLayoutManager(this,  RecyclerView.VERTICAL, false)
        adapter = AdminStaffAdapter(staffList)
        binding.rvStaff.adapter = adapter
    }


    private fun initViews() {
        staffList = ArrayList()
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        staffReference = db.collection(Constants.STAFF_DATABASE_ROOT)
    }

    private fun getStaff() {
        staffList.clear()
        binding.progressLayout.isVisible = true
        staffReference.get().addOnSuccessListener { documentSnapshots ->
            for (documentSnapshot in documentSnapshots) {
                val staff  = documentSnapshot.toObject(Admin::class.java)
                try {
                    staffList.add(staff)

                } catch (ex: Exception) {
                    binding.progressLayout.isVisible = false
                    BaseUtils.showMessage(findViewById(android.R.id.content), ex.message.toString())
                }
            }
            if (staffList.isNotEmpty()){
                adapter.updateList(staffList)
                binding.rvStaff.isVisible = true
                binding.tvNoData.isVisible = false
            }
            else{
                binding.rvStaff.isVisible = false
                binding.tvNoData.isVisible = true
            }
            binding.progressLayout.isVisible = false

        }.addOnFailureListener {
            binding.progressLayout.isVisible = false
            BaseUtils.showMessage(findViewById(android.R.id.content), it.message.toString())
        }


    }
}