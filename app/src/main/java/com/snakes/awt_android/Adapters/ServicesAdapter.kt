package com.snakes.awt_android.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.DasterkhawanItemBinding
import com.snakes.awt_android.databinding.ServiceItemBinding


class ServicesAdapter(): RecyclerView.Adapter<ServicesAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: ServiceItemBinding = ServiceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (position) {
            0 -> {
                holder.binding.ivImage.setImageResource(R.drawable.img_food_bank)
            }
            1 -> {
                holder.binding.ivImage.setImageResource(R.drawable.img_3)
            }
            2 -> {
                holder.binding.ivImage.setImageResource(R.drawable.img_4)
            }
            3 -> {
                holder.binding.ivImage.setImageResource(R.drawable.img_5)
            }
            else -> {
                holder.binding.ivImage.setImageResource(R.drawable.img_food_bank)

            }
        }

    }



    override fun getItemCount(): Int {
        return 7
    }

    inner class Holder(
        binding: ServiceItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        var binding: ServiceItemBinding = binding


    }
}