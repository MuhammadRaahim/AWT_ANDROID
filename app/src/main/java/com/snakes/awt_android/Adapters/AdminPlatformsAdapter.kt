package com.snakes.awt_android.Adapters
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.number.IntegerWidth
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.snakes.awt_android.Activities.AdminServiceActivity
import com.snakes.awt_android.Fragments.Platforms
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.ItemPlatformBinding


class AdminPlatformsAdapter(
    private var platformList: ArrayList<Platforms>,
    private var context: Context
): RecyclerView.Adapter<AdminPlatformsAdapter.Holder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: ItemPlatformBinding = ItemPlatformBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
      holder.bind(position)
    }

    override fun getItemCount(): Int {
        return platformList.size
    }

    inner class Holder(
        binding: ItemPlatformBinding
    ):RecyclerView.ViewHolder(binding.root){

        var binding: ItemPlatformBinding = binding

        fun bind(position: Int) {

            binding.apply {
                val platforms = platformList[position]
                ivImage.setImageResource(platforms.icon)
                card.setBackgroundColor(Color.parseColor(platforms.backgroundColor))
                tvPlatformName.text = platforms.title


                itemView.setOnClickListener {
                    when(position){
                        0 ->{
                            Navigation.findNavController(itemView).navigate(R.id.navigation_admin_profile)
                        }
                        1 ->{
                            context.startActivity(Intent(context, AdminServiceActivity::class.java))
                        }
                    }
                }



            }

        }


    }
}