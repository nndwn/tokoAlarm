package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterListNada(
    private val listNada: List<Tone>,
    private val click : View.OnClickListener
) :
    RecyclerView.Adapter<AdapterListNada.ViewHolderNada>() {
    class ViewHolderNada(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView : TextView = itemView.findViewById(R.id.textBtn)
        val bg : LinearLayout = itemView.findViewById(R.id.container_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNada {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_button, parent, false)
        return ViewHolderNada(view)
    }

    override fun onBindViewHolder(holder: ViewHolderNada, position: Int) {
        holder.textView.text = listNada[position].name
        holder.bg.setOnClickListener(click)
    }

    override fun getItemCount() = listNada.size
}