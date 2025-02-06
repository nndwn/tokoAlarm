package com.example.tokoalarm

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


interface OnItemClickAdapterListDetail {
    fun onItemRename(position: Int)
    fun onItemMonitoring(position: Int)
    fun onItemPerpanjang(position: Int)
    fun onItemAdd()
}

class AdapterListDetail(
    private val listDetail: List<ListAlat>,
    private val listener: OnItemClickAdapterListDetail
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    class ViewHolderDetail(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameAlat: TextView = itemView.findViewById(R.id.namePerangkat)
        private val rename: Button = itemView.findViewById(R.id.rename)
        private val statusResult: TextView = itemView.findViewById(R.id.statusResult)
        private val exp: TextView = itemView.findViewById(R.id.expResult)
        private val idAlat: TextView = itemView.findViewById(R.id.idAlatResult)
        private val namePaket: TextView = itemView.findViewById(R.id.namePaketResult)
        private val mulai: TextView = itemView.findViewById(R.id.mulaiResult)
        private val akhir: TextView = itemView.findViewById(R.id.akhirReult)
        private val monitoring: Button = itemView.findViewById(R.id.btnMonitoring)
        private val button2: Button = itemView.findViewById(R.id.btnPerpanjang)

        fun bind(detail: ListAlat, click: OnItemClickAdapterListDetail) {
            statusResult.text = detail.status
            exp.text = buildString {
                append(detail.sisaHari)
                append(" ")
                append(itemView.context.getString(R.string.hari))
            }
            idAlat.text = detail.idAlat
            namePaket.text = detail.periode
            mulai.text = detail.tanggalMulai
            akhir.text = detail.tanggalSelesai

            if (detail.namePaket == "-" || detail.namePaket.isEmpty()) {
                nameAlat.text = buildString {
                    append(itemView.context.getString(R.string.alat))
                    append(" ")
                    append(detail.idAlat)
                }
            } else {
                nameAlat.text = detail.namePaket
            }

            when (detail.status) {
                "Aktif" -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        statusResult.setTextColor(itemView.context.getColor(R.color.text_success))
                    }
                }

                "Non Aktif" -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        statusResult.setTextColor(itemView.context.getColor(R.color.text_failed))
                        exp.visibility = View.GONE
                        val expText = itemView.findViewById<TextView>(R.id.exp)
                        expText.visibility = View.GONE
                    }
                }

                "Pending" -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        statusResult.setTextColor(itemView.context.getColor(R.color.text_pending))
                    }
                }
            }

            rename.setOnClickListener {
                click.onItemRename(bindingAdapterPosition)
            }
            monitoring.setOnClickListener {
                click.onItemMonitoring(bindingAdapterPosition)
            }
            button2.setOnClickListener {
                click.onItemPerpanjang(bindingAdapterPosition)
            }
        }
    }

    class ViewHolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val addBtn: Button = itemView.findViewById(R.id.btn_add)
        fun bind (click: OnItemClickAdapterListDetail) {
            addBtn.setOnClickListener {
                click.onItemAdd()
            }
            addBtn.text = itemView.context.getString(R.string.tambah_perangkat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.layout_button_add, parent, false)
                ViewHolderHeader(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.adapter_item_detail, parent, false)
                ViewHolderDetail(view)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolderHeader -> {
                holder.bind(listener)
            }
            is ViewHolderDetail -> {
                holder.bind(listDetail[position - 1], listener)
            }
        }
    }

    override fun getItemCount() = listDetail.size + 1
}