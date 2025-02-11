package com.example.tokoalarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView

interface OnListenerSensor {
    fun rename (position: Int)
    fun switcher (position: Int, isChecked :Boolean)
    fun check (check : Boolean)
}

class AdapterListSensor(
    private val listSensor: List<ListSensor>,
    private val listener: OnListenerSensor
) : RecyclerView.Adapter<AdapterListSensor.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.name)
        private val rename = view.findViewById<TextView>(R.id.rename)
        private val indicator = view.findViewById<ImageView>(R.id.indicator)
        private val condition = view.findViewById<TextView>(R.id.condition)
        private val switcher = view.findViewById<SwitchCompat>(R.id.switcher)
        private val background = view.findViewById<LinearLayout>(R.id.switcher_background)

        var check = false


        private fun switherAktif () {
            indicator.backgroundTintList = itemView.context.getColorStateList(R.color.text_success)
            condition.text = itemView.context.getString(R.string.aktif)
            condition.contentDescription = itemView.context.getString(R.string.aktif)
            background.backgroundTintList = itemView.context.getColorStateList(R.color.bg_success)
        }

        private fun switherTidakAktif () {
            indicator.backgroundTintList = itemView.context.getColorStateList(R.color.text_failed)
            condition.text = itemView.context.getString(R.string.no_active)
            condition.contentDescription = itemView.context.getString(R.string.no_active)
            background.backgroundTintList = itemView.context.getColorStateList(R.color.bg_failed)
        }

        fun bind(sensor: ListSensor, listener: OnListenerSensor) {
            name.text = sensor.rename.ifEmpty { sensor.name }
            rename.setOnClickListener {
                listener.rename(bindingAdapterPosition)
            }
            switcher.isChecked = sensor.stsstatin == "1"
            check = sensor.stsstatin == "1"
            if (switcher.isChecked) {
                switherAktif()
            } else {
               switherTidakAktif()
            }

            listener.check(check)
            switcher.setOnCheckedChangeListener { _, isChecked ->
                listener.switcher(bindingAdapterPosition, isChecked)
                if (isChecked) {
                    check = true
                    switherAktif()
                } else {
                    check = false
                    switherTidakAktif()
                }
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_item_sensor, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sensor = listSensor[position]
        holder.bind(sensor, listener)
    }
    override fun getItemCount() = listSensor.size
}