package com.example.tokoalarm

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tokoalarm.databinding.ActivityMonitoringBinding
import com.example.tokoalarm.databinding.AdapterItemJadwalBinding


class ActivityMonitoring :AppCompatActivity() {


    private lateinit var koneksi : TextView
    private lateinit var listAlat: ListAlat
    private var listJadwal = ArrayList<ListJadwal>()
    private lateinit var utils : Utils

    private lateinit var binding: ActivityMonitoringBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<TextView>(R.id.textMenu).text = getString(R.string.monitoring)
        findViewById<View>(R.id.toolbar).setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        utils = Utils(this)

        listAlat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("MonitorData", ListAlat::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("MonitorData")!!
        }

        listJadwal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("JadwalData", ListJadwal::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("JadwalData")!!
        }

        binding.namaAlatResult.text = utils.checkNameAlat(listAlat.namePaket, listAlat.idAlat)
        binding.idAlatResult.text = listAlat.idAlat

        val statusView = binding.statusPaketResult
        statusView.text = listAlat.status

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (listAlat.status) {
                "Non Aktif" -> statusView.setTextColor(getColor(R.color.text_failed))
                "Aktif" -> statusView.setTextColor(getColor(R.color.text_success))
                "Pending" -> statusView.setTextColor(getColor(R.color.text_pending))
            }
        }
        binding.mulaiResult.text = listAlat.tanggalMulai
        binding.akhirResult.text = listAlat.tanggalSelesai

        listJadwal.filter {
            it.idAlat == listAlat.idAlat
        }.forEach {
            binding.tambahJadwal.visibility = View.GONE
            binding.layoutJadwal.root.visibility = View.VISIBLE

        }

        binding.checkBtn.setOnClickListener {

        }

        binding.checkAlatBtn.setOnClickListener {

        }

        binding.tambahJadwal.setOnClickListener {

        }

        koneksi = findViewById(R.id.koneksi)


    }

}