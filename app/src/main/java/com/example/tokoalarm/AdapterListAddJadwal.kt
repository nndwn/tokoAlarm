package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterListAddJadwal (
    private val listAddJadwal: List<ListAddJadwal> ,
    private val click : (position :Int) -> Unit
    ): RecyclerView.Adapter<AdapterListAddJadwal.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaPerangkat: TextView = itemView.findViewById(R.id.namaPerangkat)
        private val mulaiResult :TextView = itemView.findViewById(R.id.tanggalMulaiResult)
        private val akhirResult :TextView = itemView.findViewById(R.id.waktuAkhirResult)
        private val sisaHariResult :TextView = itemView.findViewById(R.id.sisaHariResult)

        private val utils = Utils(itemView.context)

        fun bind(list: ListAddJadwal) {
            namaPerangkat.text = utils.checkNameAlat(list.namaAlat, list.idAlat)

            mulaiResult.text = list.tanggalMulai
            akhirResult.text = list.tanggalSelesai
            sisaHariResult.text = list.sisaHari
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_add_jadwal, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listAddJadwal[position])
        holder.itemView.setOnClickListener{
            click(position)
        }
    }

    override fun getItemCount() = listAddJadwal.size
}