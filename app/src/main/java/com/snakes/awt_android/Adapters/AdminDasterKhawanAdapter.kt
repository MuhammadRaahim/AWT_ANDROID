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
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.Models.Service
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.DasterkhawanItemBinding
import com.snakes.awt_android.databinding.ServiceItemBinding
import java.io.Serializable


class AdminDasterKhawanAdapter(

    private var daterKhawanList: ArrayList<DasterKhawan>

): RecyclerView.Adapter<AdminDasterKhawanAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: DasterkhawanItemBinding = DasterkhawanItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.bind(position)
    }



    override fun getItemCount(): Int {
        return daterKhawanList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<DasterKhawan>){
        daterKhawanList = list
        notifyDataSetChanged()
    }

    inner class Holder(
        binding: DasterkhawanItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        var binding: DasterkhawanItemBinding = binding

        fun bind(position: Int) {

            val dasterKhawan = daterKhawanList[position]

            Glide.with(itemView.context).load(dasterKhawan.photo)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(binding.ivEvent)
            binding.tvEventTitle.text = dasterKhawan.name


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DasterKhawanDetailsActivity::class.java)
                intent.putExtra(DASTERKHAWAN_OBJECT, dasterKhawan as Serializable)
                itemView.context.startActivity(intent)
            }

        }


    }
}