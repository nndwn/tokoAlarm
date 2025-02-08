package com.example.tokoalarm

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tokoalarm.databinding.ActivityMonitoringBinding


class ActivityMonitoring : AppCompatActivity() {


    private lateinit var listAlat: ListAlat
    private var listJadwal = ArrayList<ListJadwal>()
    private lateinit var utils: Utils

    private lateinit var mqtt: HandlerMqtt


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitoring)
        mqtt = HandlerMqtt()

        findViewById<TextView>(R.id.textMenu).text = getString(R.string.monitoring)
        findViewById<View>(R.id.toolbar).setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else onBackPressedDispatcher.onBackPressed()
       }



//
//        utils = Utils(this)
//
//        listAlat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra("MonitorData", ListAlat::class.java)!!
//        } else {
//            @Suppress("DEPRECATION")
//            intent.getParcelableExtra("MonitorData")!!
//        }
//
//        listJadwal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableArrayListExtra("JadwalData", ListJadwal::class.java)!!
//        } else {
//            @Suppress("DEPRECATION")
//            intent.getParcelableArrayListExtra("JadwalData")!!
//        }
//
//        binding.namaAlatResult.text = utils.checkNameAlat(listAlat.namePaket, listAlat.idAlat)
//        binding.idAlatResult.text = listAlat.idAlat
//
//        val statusView = binding.statusPaketResult
//        statusView.text = listAlat.status
//
//        when (listAlat.status) {
//            "Non Aktif" -> statusView.setTextColor(getColor(R.color.text_failed))
//            "Aktif" -> statusView.setTextColor(getColor(R.color.text_success))
//            "Pending" -> statusView.setTextColor(getColor(R.color.text_pending))
//        }
//        binding.mulaiResult.text = listAlat.tanggalMulai
//        binding.akhirResult.text = listAlat.tanggalSelesai
//
//        listJadwal.filter {
//            it.idAlat == listAlat.idAlat
//        }.forEach {
//            binding.tambahJadwal.visibility = View.GONE
//            binding.layoutJadwal.root.visibility = View.VISIBLE
//            binding.layoutJadwal.namaPerangkat.text = utils.checkNameAlat(it.namePaket, it.idAlat)
//            binding.layoutJadwal.hariResult.text = dayName(it.days)
//            binding.layoutJadwal.resultWaktuMulai.text = buildString {
//                append(it.startTime)
//                append(" ")
//                append("WIB")
//            }
//            binding.layoutJadwal.resultWaktuMulai.text = buildString {
//                append(it.endTime)
//                append(" ")
//                append("WIB")
//            }
//            binding.layoutJadwal.switcher.setOnClickListener {
//                //todo: jadwal switch belum diisi fungsi
//            }
//            binding.layoutJadwal.hapus.setOnClickListener {
//                //todo: jadwal hapus belum diisi fungsi
//            }
//            binding.layoutJadwal.edit.setOnClickListener {
//                //todo: jadwal edit belum diisi fungsi
//            }
//
//        }
//
//        mqtt.connect { isConnected ->
//            runOnUiThread {
//                checkServer(isConnected)
//            }
//
//        }
//
//        mqtt.subscribe(listAlat.idAlat + "/active") {
//            runOnUiThread {
//                checkAlat(it)
//            }
//        }
//
//        binding.checkBtn.setOnClickListener {
//            mqtt.disconnect()
//            mqtt.connect {
//                checkServer(it)
//            }
//        }
//
//        val mode = binding.checkAlatBtn
//        mode.text = getString(R.string.mode_auto)
//        binding.checkAlatBtn.setOnClickListener {
//            if (mode.text == getString(R.string.mode_auto)) {
//                mode.text = getString(R.string.mode_manual)
//                mqtt.publish(listAlat.idAlat + "/mode", "manual")
//            } else {
//                mode.text = getString(R.string.mode_auto)
//                mqtt.publish(listAlat.idAlat + "/mode", "otomatis")
//            }
//        }
//
//        binding.tambahJadwal.setOnClickListener {
//            //todo: jadwal edit belum diisi fungsi
//        }
    }

//    private fun checkServer(boolean: Boolean) {
//        val koneksi = binding.koneksi
//        val indicator = binding.indicator
//        if (boolean) {
//            koneksi.text = getString(R.string.terhubung_server)
//            koneksi.setTextColor(getColor(R.color.text_success))
//            koneksi.contentDescription = getString(R.string.terhubung_server)
//            indicator.backgroundTintList = getColorStateList(R.color.text_success)
//            indicator.contentDescription = getString(R.string.terhubung_server)
//        } else {
//            koneksi.text = getString(R.string.tidak_terhubung_server)
//            koneksi.setTextColor(getColor(R.color.text_failed))
//            koneksi.contentDescription = getString(R.string.tidak_terhubung_server)
//            indicator.backgroundTintList = getColorStateList(R.color.text_failed)
//            indicator.contentDescription = getString(R.string.tidak_terhubung_server)
//        }
//    }

//    private fun checkAlat(result: String) {
//        val check = binding.statusAlatResult
//        if (result == "1") {
//            check.text = getString(R.string.aktif)
//            check.contentDescription = getString(R.string.aktif)
//            check.setTextColor(getColor(R.color.text_success))
//        } else {
//            check.text = getString(R.string.no_active)
//            check.contentDescription = getString(R.string.no_active)
//            check.setTextColor(getColor(R.color.text_failed))
//        }
//
//    }
}