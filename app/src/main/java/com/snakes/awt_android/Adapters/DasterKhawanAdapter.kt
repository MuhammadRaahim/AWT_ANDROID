package com.snakes.awt_android.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.snakes.awt_android.CallBacks.OnItemClickListener
import com.snakes.awt_android.databinding.DasterkhawanItemBinding


class DasterKhawanAdapter(

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
        return 7
    }

    inner class Holder(
        binding: DasterkhawanItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        var binding: DasterkhawanItemBinding = binding

        fun bind(position: Int) {
          itemView.setOnClickListener {
              onItemClickListener.onItemClick()
          }
        }

    }
}