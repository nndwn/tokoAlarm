package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterListSensor(private val listSensor: List<ListSensor>) : RecyclerView.Adapter<AdapterListSensor.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val rename = view.findViewById<TextView>(R.id.rename)
        private val indicator = view.findViewById<TextView>(R.id.indicator)
        private val condition = view.findViewById<TextView>(R.id.condition)
        private val switcher = view.findViewById<TextView>(R.id.switcher)

        fun bind(sensor: ListSensor) {

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_sensor, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sensor = listSensor[position]
    }
    override fun getItemCount() = listSensor.size
}