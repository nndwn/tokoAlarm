package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickAdapterListJadwal {
    fun onItemEdit(position: Int)
    fun onItemDelete(position: Int)
    fun onItemSwitch(position: Int)
    fun onItemAdd()
}


class AdapterListJadwal(
    private val listJadwal: List<ListJadwal>,
    private val listener: OnItemClickAdapterListJadwal
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }

    class ViewHolderListJadwal(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaPerangkat: TextView = itemView.findViewById(R.id.namaPerangkat)
        private val hari: TextView = itemView.findViewById(R.id.hariResult)
        private val waktuMulai: TextView = itemView.findViewById(R.id.resultWaktuMulai)
        private val waktuBerakhir: TextView = itemView.findViewById(R.id.resultWaktuBerakhir)
        private val editBtn: Button = itemView.findViewById(R.id.edit)
        private val deleteBtn: Button = itemView.findViewById(R.id.hapus)
        private val switchBtn: SwitchCompat = itemView.findViewById(R.id.switcher)

        private val utils = Utils(itemView.context)

        fun bind(jadwal: ListJadwal, click: OnItemClickAdapterListJadwal) {
            namaPerangkat.text = utils.checkNameAlat(jadwal.namePaket, jadwal.idAlat)
            hari.text =  convertDayName(jadwal.days)
            waktuMulai.text = buildString {
                append(jadwal.startTime)
                append(" ")
                append("WIB")
            }
            waktuBerakhir.text = buildString {
                append(jadwal.endTime)
                append(" ")
                append("WIB")
            }

            editBtn.setOnClickListener {
                click.onItemEdit(bindingAdapterPosition)
            }
            deleteBtn.setOnClickListener {
                click.onItemDelete(bindingAdapterPosition)
            }
            switchBtn.setOnClickListener {
                click.onItemSwitch(bindingAdapterPosition)
            }
        }
    }

    class HeaderViewHolder(itemView: View) :RecyclerView.ViewHolder (itemView) {
        private val addBtn: Button = itemView.findViewById(R.id.btn_add)
        fun bind(click: OnItemClickAdapterListJadwal) {
            addBtn.setOnClickListener {
                click.onItemAdd()
            }
            addBtn.text = itemView.context.getString(R.string.tambah_jadwal)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_button_add, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_item_jadwal, parent, false)
                ViewHolderListJadwal(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is HeaderViewHolder -> {
                holder.bind(listener)
            }
            is ViewHolderListJadwal -> {
                holder.bind(listJadwal[position - 1], listener)
            }
        }
    }

    override fun getItemCount() = listJadwal.size + 1
}