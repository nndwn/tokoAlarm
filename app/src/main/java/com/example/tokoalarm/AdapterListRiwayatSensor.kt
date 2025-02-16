package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterListRiwayatSensor (
    private val listSensor: List<ListSensor>
) : RecyclerView.Adapter<AdapterListRiwayatSensor.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val namaSensor = view.findViewById<TextView>(R.id.riwayatSensorName)
        private val tanggal = view.findViewById<TextView>(R.id.TanggalAkhirTedeteksiResult)
        private val waktu = view.findViewById<TextView>(R.id.WaktuAkhirTedeteksiResult)

        fun bind (list: ListSensor) {
            if (list.inn != "null" ) {
                namaSensor.text = buildString {
                    append(itemView.context.getString(R.string.riwayat))
                    append(" ")
                    if (list.rename.isNotEmpty()) {
                        append(list.rename)
                    } else append(list.name)
                }
                val (date, time) = list.inn.split(" ")
                tanggal.text = date
                waktu.text = time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_riwayat_sensor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSensor[position])
    }
    override fun getItemCount(): Int {
       return listSensor.filter { it.inn != "null"  }.size
    }
}