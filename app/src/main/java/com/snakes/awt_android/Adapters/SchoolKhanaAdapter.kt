package com.snakes.awt_android.Adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.snakes.awt_android.CallBacks.OnItemClickListener
import com.snakes.awt_android.databinding.DasterkhawanItemBinding
import com.snakes.awt_android.databinding.SchoolKhanaItemBinding


class SchoolKhanaAdapter(



): RecyclerView.Adapter<SchoolKhanaAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding: SchoolKhanaItemBinding = SchoolKhanaItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 7
    }

    inner class Holder(
        binding: SchoolKhanaItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        var binding: SchoolKhanaItemBinding = binding

    }
}