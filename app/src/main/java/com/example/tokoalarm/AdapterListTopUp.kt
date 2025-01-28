package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterListTopUp(
    private val listTopUp: List<Price>,
    private  val onclick : View.OnClickListener
) :
    RecyclerView.Adapter<AdapterListTopUp.ViewHolderTopUp>() {
    class ViewHolderTopUp(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textBtn)
        val bgView :LinearLayout = itemView.findViewById(R.id.container_btn)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderTopUp {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_button, parent, false)
        return ViewHolderTopUp(view)
    }
    override fun onBindViewHolder(holder: ViewHolderTopUp, position: Int) {
        holder.textView.text = formatRupiah(listTopUp[position].price)
        holder.bgView.setOnClickListener(onclick)
    }
    override fun getItemCount() = listTopUp.size
}