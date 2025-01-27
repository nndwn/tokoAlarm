package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterListPayment(
    private val listPayment: List<BankAccount>,
    private val click : OnClickListener)
    :RecyclerView.Adapter<AdapterListPayment.ViewHolderPayment>() {
    class ViewHolderPayment(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val textView :TextView = itemView.findViewById(R.id.textBtn)
        val bg : LinearLayout = itemView.findViewById(R.id.container_btn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolderPayment {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_button, parent, false)
        return ViewHolderPayment(view)
    }
    override fun onBindViewHolder(holder :ViewHolderPayment, position : Int) {
        holder.textView.text = listPayment[position].name
        holder.bg.setOnClickListener(click)
    }
    override fun getItemCount() = listPayment.size
}