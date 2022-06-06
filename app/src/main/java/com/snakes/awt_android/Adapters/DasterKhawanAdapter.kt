package com.snakes.awt_android.Adapters
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snakes.awt_android.CallBacks.OnItemClickListener
import com.snakes.awt_android.Models.DasterKhawan
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.DasterkhawanItemBinding


class DasterKhawanAdapter(
    private var daterKhawanList: ArrayList<DasterKhawan>,
    var onItemClickListener: OnItemClickListener

): RecyclerView.Adapter<DasterKhawanAdapter.Holder>() {

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
       var binding: DasterkhawanItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        fun bind(position: Int) {

            val dasterKhawan = daterKhawanList[position]

            Glide.with(itemView.context).load(dasterKhawan.photo)
                .placeholder(R.drawable.img_profile_cover_placeholder)
                .into(binding.ivEvent)
            binding.tvEventTitle.text = dasterKhawan.name
            binding.tvLocation.text = dasterKhawan.locatoion
            binding.tvDate.text = dasterKhawan.date

            itemView.setOnClickListener {
              onItemClickListener.onItemClick(dasterKhawan)
          }
        }

    }
}