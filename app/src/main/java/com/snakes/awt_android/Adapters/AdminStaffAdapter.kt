package com.snakes.awt_android.Adapters
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants.Companion.DASTERKHAWAN_OBJECT
import com.horizam.skbhub.Utils.Constants.Companion.SERVICE_OBJECT
import com.snakes.awt_android.Activities.DasterKhawanDetailsActivity
import com.snakes.awt_android.Activities.ServiceDetailActivity
import com.snakes.awt_android.Models.Admin
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.Models.User
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.DasterkhawanItemBinding
import com.snakes.awt_android.databinding.ServiceItemBinding
import com.snakes.awt_android.databinding.StaffItemBinding
import java.io.Serializable


class AdminStaffAdapter(

    private var staffList: ArrayList<Admin>

): RecyclerView.Adapter<AdminStaffAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: StaffItemBinding = StaffItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.bind(position)
    }

    override fun getItemCount(): Int {
        return staffList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<Admin>){
        staffList = list
        notifyDataSetChanged()
    }

    inner class Holder(
         var binding:StaffItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int) {

            val staff = staffList[position]

            Glide.with(itemView.context).load(staff.profileImage)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(binding.ivIcon)
            binding.tvTittle.text = staff.userName

//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, DasterKhawanDetailsActivity::class.java)
//                intent.putExtra(DASTERKHAWAN_OBJECT, dasterKhawan as Serializable)
//                itemView.context.startActivity(intent)
//            }

        }


    }
}