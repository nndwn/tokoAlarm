package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterListPromo(
    private val promoList :List<String>,
    private val click :View.OnClickListener

) :RecyclerView.Adapter<AdapterListPromo.ViewHolderPromo>() {

    class ViewHolderPromo(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.promoView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPromo {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_promo, parent, false)
        return ViewHolderPromo(view)
    }

    override fun onBindViewHolder(holder: ViewHolderPromo, position: Int) {
        val imageUrl = promoList[position]
        holder.imageView.setOnClickListener(click)
        Glide.with(holder.imageView.context)
            .load(imageUrl)
            .into(holder.imageView)

    }

    override fun getItemCount() = promoList.size
}