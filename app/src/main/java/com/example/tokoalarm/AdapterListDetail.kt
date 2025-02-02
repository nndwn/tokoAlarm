package com.example.tokoalarm

import android.media.MediaDrm.OnKeyStatusChangeListener
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemRename(position: Int)
    fun onItemMonitoring(position: Int)
    fun onItemPerpanjang(position: Int)
}

class AdapterListDetail(
    private val listDetail : List<ListAlat>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<AdapterListDetail.ViewHolderDetail>() {
    class ViewHolderDetail(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameAlat :TextView  = itemView.findViewById(R.id.namePerangkat)
        private val rename : Button = itemView.findViewById(R.id.rename)
        private val statusResult : TextView = itemView.findViewById(R.id.statusResult)
        private val exp : TextView = itemView.findViewById(R.id.expResult)
        private val idAlat : TextView = itemView.findViewById(R.id.idAlatResult)
        private val namePaket : TextView = itemView.findViewById(R.id.namePaketResult)
        private val mulai : TextView = itemView.findViewById(R.id.mulaiResult)
        private val akhir : TextView = itemView.findViewById(R.id.akhirReult)
        private val monitoring : Button = itemView.findViewById(R.id.btnMonitoring)
        private val button2 : Button = itemView.findViewById(R.id.btnPerpanjang)

        fun bind(detail : ListAlat, click : OnItemClickListener) {
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

            rename.setOnClickListener{
                click.onItemRename(bindingAdapterPosition)
            }
            monitoring.setOnClickListener{
                click.onItemMonitoring(bindingAdapterPosition)
            }
            button2.setOnClickListener{
                click.onItemPerpanjang(bindingAdapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDetail {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_detail, parent, false)
        return ViewHolderDetail(view)
    }

    override fun onBindViewHolder(holder: ViewHolderDetail, position: Int) {
        holder.bind(listDetail[position], listener)
    }
    override fun getItemCount() = listDetail.size
}