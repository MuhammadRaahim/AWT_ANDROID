package com.snakes.awt_android.Adapters
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants.Companion.DASTERKHAWAN_OBJECT
import com.horizam.skbhub.Utils.Constants.Companion.SCHOOLKHANA_OBJECT
import com.horizam.skbhub.Utils.Constants.Companion.SERVICE_OBJECT
import com.horizam.skbhub.Utils.Constants.Companion.STAFF
import com.snakes.awt_android.Activities.AdminSchoolDetailsActivity
import com.snakes.awt_android.Activities.AdminStaffDetailsActivity
import com.snakes.awt_android.Activities.DasterKhawanDetailsActivity
import com.snakes.awt_android.Activities.ServiceDetailActivity
import com.snakes.awt_android.Models.*
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.DasterkhawanItemBinding
import com.snakes.awt_android.databinding.SchoolKhanaItemBinding
import com.snakes.awt_android.databinding.ServiceItemBinding
import com.snakes.awt_android.databinding.StaffItemBinding
import java.io.Serializable


class AdminSchoolAdapter(

    private var schoolList: ArrayList<SchoolKhana>

): RecyclerView.Adapter<AdminSchoolAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: SchoolKhanaItemBinding = SchoolKhanaItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.bind(position)
    }

    override fun getItemCount(): Int {
        return schoolList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<SchoolKhana>){
        schoolList = list
        notifyDataSetChanged()
    }

    inner class Holder(
         var binding:SchoolKhanaItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int) {

            val school = schoolList[position]


            Glide.with(itemView.context).load(school.photo)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(binding.ivImage)
            binding.tvName.text = school.name
            binding.tvLocation.text = school.locatoion
            binding.tvTime.text = "${school.startTime} pm to ${school.endTime} pm"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, AdminSchoolDetailsActivity::class.java)
                intent.putExtra(SCHOOLKHANA_OBJECT, school as Serializable)
                itemView.context.startActivity(intent)
            }

        }


    }
}