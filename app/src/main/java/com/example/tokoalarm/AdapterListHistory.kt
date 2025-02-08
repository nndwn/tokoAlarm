package com.example.tokoalarm

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterListHistory(private val listHistory: List<ListTopUpData>) :
    RecyclerView.Adapter<AdapterListHistory.ViewHolderHistory>() {
    class ViewHolderHistory(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNoReff: TextView = itemView.findViewById(R.id.tvNoreff)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvKeterangan: TextView = itemView.findViewById(R.id.tvKeterangan)
        private val tvTipe: TextView = itemView.findViewById(R.id.tvTipe)
        private val tvJumlah: TextView = itemView.findViewById(R.id.tvJumlah)
        private val tvSaldoAwal: TextView = itemView.findViewById(R.id.tvSaldoAwal)
        private val tvSaldoAkhir: TextView = itemView.findViewById(R.id.tvSaldoAkhir)
        private val tvUpdateAt: TextView = itemView.findViewById(R.id.tvUpdatedAt)

        @SuppressLint("SetTextI18n")
        fun bind(history: ListTopUpData) {
            tvNoReff.text = history.noreff
            tvKeterangan.text = history.keterangan
            tvTipe.text = history.tipe
            tvSaldoAwal.text = history.saldoAwal
            tvSaldoAkhir.text = history.saldoAkhir
            tvUpdateAt.text = history.updatedAt
            tvStatus.text = history.status

            if (history.tipe == "Kredit") {
                tvJumlah.setTextColor(
                    itemView.context.getColor(
                        R.color.text_success
                    )
                )
                tvJumlah.text = "+ ${history.jumlah}"
            } else {
                tvJumlah.setTextColor(
                    itemView.context.getColor(
                        R.color.text_failed
                    )
                )
                tvJumlah.text = "- ${history.jumlah}"
            }

            when (history.status) {
                "Success" -> {
                    tvStatus.setTextColor(itemView.context.getColor(R.color.text_success))
                    tvStatus.backgroundTintList =
                        itemView.context.getColorStateList(R.color.bg_success)
                }

                "Failed" -> {
                    tvStatus.setTextColor(itemView.context.getColor(R.color.text_failed))
                    tvStatus.backgroundTintList =
                        itemView.context.getColorStateList(R.color.bg_failed)
                }

                "Pending" -> {
                    tvStatus.setTextColor(itemView.context.getColor(R.color.text_pending))
                    tvStatus.backgroundTintList =
                        itemView.context.getColorStateList(R.color.bg_pending)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHistory {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_item_history, parent, false)
        return ViewHolderHistory(view)
    }

    override fun onBindViewHolder(holder: ViewHolderHistory, position: Int) {
        holder.bind(listHistory[position])
    }

    override fun getItemCount() = listHistory.size
}