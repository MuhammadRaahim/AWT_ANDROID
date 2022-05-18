package com.snakes.awt_android.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.horizam.skbhub.Utils.Constants
import com.horizam.skbhub.Utils.PrefManager
import com.snakes.awt_android.Activities.AuthActivity
import com.snakes.awt_android.Adapters.AdminPlatformsAdapter
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.FragmentAdminDashBoardBinding
import java.io.Serializable

class AdminDashBoardFragment : Fragment() {

    private lateinit var binding: FragmentAdminDashBoardBinding
    private lateinit var platformList: ArrayList<Platforms>
    private lateinit var adapter: AdminPlatformsAdapter
    private lateinit var manager: PrefManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminDashBoardBinding.inflate(layoutInflater)

        initViews()
        setRecyclerView()
        setClickListeners()

        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            icLogout.setOnClickListener {
                FirebaseAuth.getInstance().signOut()
                manager.session = Constants.LOGOUT
                startActivity(Intent(requireContext(), AuthActivity::class.java))
                activity!!.finish()
            }
        }
    }

    private fun setRecyclerView() {
        binding.rvPlatforms.layoutManager =  GridLayoutManager(requireContext(),2,  RecyclerView.VERTICAL, false)
        adapter = AdminPlatformsAdapter(platformList,requireContext())
        binding.rvPlatforms.adapter = adapter
    }

    private fun initViews() {
        manager = PrefManager(requireContext())
        platformList = ArrayList()
        platformList.add(Platforms("1", "Profile", R.drawable.ic_profile_filled,"","#ffffff", "",""))
        platformList.add(Platforms("2", "Services", R.drawable.ic_services,"","#ffffff", "Enter the phone number you want the tag to display", "Phone Number"))
        platformList.add(Platforms("3", "Staff", R.drawable.ic_staff,"","#ffffff", "Enter the Social Media URL you want the tag to display","Facetime URL"))
        platformList.add(Platforms("4", "Analytics", R.drawable.ic_analytics,"","#ffffff", "Enter the Email Address you want the tag to display","Email Address"))
        platformList.add(Platforms("5", "DasterKhawan", R.drawable.ic_food_bank,"","#ffffff", "Enter the Website Address you want the tag to display","Website Address"))
        platformList.add(Platforms("6", "School-Khana Program", R.drawable.ic_school,"","#ffffff", "Enter the Text/SMS Number you want the tag to display","Text/SMS Number"))
    }

}

data class Platforms(
    val id : String,
    val title : String,
    val icon : Int,
    val baseURL : String,
    val backgroundColor: String,
    val description_sv : String,
    val placeholder: String
): Serializable