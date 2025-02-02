package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class AdapterListPaket(
    private val paket: List<ListPaket>,
    private val click: View.OnClickListener
) : RecyclerView.Adapter<AdapterListPaket.ViewHolderPaket>() {
    class ViewHolderPaket(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentView: ConstraintLayout = itemView.findViewById(R.id.clickPaket)
        val durasiResultView: TextView = itemView.findViewById(R.id.durasi_result)
        val hargaResultView: TextView = itemView.findViewById(R.id.harga_result)
        val nameView: TextView = itemView.findViewById(R.id.namePaket)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderPaket {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_paket, parent, false)
        return ViewHolderPaket(view)
    }

    override fun onBindViewHolder(holder: ViewHolderPaket, position: Int) {
        holder.nameView.text = paket[position].periode
        holder.durasiResultView.text = buildString {
            append(paket[position].dayConvertion)
            append(" ")
            append(holder.itemView.context.getString(R.string.hari))
        }
        holder.hargaResultView.text = paket[position].biayaRupiah
        holder.contentView.setOnClickListener (click)
    }

    override fun getItemCount() = paket.size
}