package com.snakes.awt_android.Adapters
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.horizam.skbhub.Utils.Constants.Companion.SERVICE_OBJECT
import com.snakes.awt_android.Activities.ServiceDetailActivity
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.DasterkhawanItemBinding
import com.snakes.awt_android.databinding.ServiceItemBinding
import java.io.Serializable


class ServicesAdapter(
    private var serviceList: ArrayList<Service>
): RecyclerView.Adapter<ServicesAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: ServiceItemBinding = ServiceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.bind(position)
    }



    override fun getItemCount(): Int {
        return serviceList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<Service>){
        serviceList = list
        notifyDataSetChanged()
    }

    inner class Holder(
        binding: ServiceItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        var binding: ServiceItemBinding = binding

        fun bind(position: Int) {

            val service = serviceList[position]

            Glide.with(itemView.context).load(service.serviceImage)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(binding.ivImage)
            binding.tvServiceName.text = service.serviceName


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ServiceDetailActivity::class.java)
                intent.putExtra(SERVICE_OBJECT, service as Serializable)
                itemView.context.startActivity(intent)
            }

        }


    }
}