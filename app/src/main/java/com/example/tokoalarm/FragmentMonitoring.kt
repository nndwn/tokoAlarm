package com.example.tokoalarm

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class FragmentMonitoring : Fragment(R.layout.fragment_monitoring) {

    private lateinit var viewMonitoring: SharedViewMonitoring
    private lateinit var mqtt: HandlerMqtt
    private lateinit var utils: Utils
    private lateinit var alert: DialogAlert
    private lateinit var success: DialogSuccess

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewMonitoring = ViewModelProvider(requireActivity())[SharedViewMonitoring::class.java]
        mqtt = HandlerMqtt()
        utils = Utils(requireContext())
        success = DialogSuccess(requireActivity())

        viewMonitoring.alat.observe(viewLifecycleOwner) { alat ->
            view.findViewById<TextView>(R.id.namaAlatResult)
                .text = utils.checkNameAlat(alat.namePaket, alat.idAlat)
            view.findViewById<TextView>(R.id.idAlatResult)
                .text = alat.idAlat
            val statusPaket = view.findViewById<TextView>(R.id.statusPaketResult)
            statusPaket.text = alat.status
            val containerStatusAlat = view.findViewById<LinearLayout>(R.id.container4)
            containerStatusAlat.visibility = View.GONE
            when (alat.status) {
                "Non Aktif" -> {
                    statusPaket.setTextColor(view.context.getColor(R.color.text_failed))
                    checkServer(view, false)
                }
                "Aktif" -> {

                    statusPaket.setTextColor(view.context.getColor(R.color.text_success))
                    containerStatusAlat.visibility = View.VISIBLE
                    mqtt.disconnect()
                    mqtt.connect { isConnected ->
                        checkServer(view, isConnected)
                    }
                    mqtt.subscribe(alat.idAlat + "/active") {
                        checkAlat(it, view)
                    }
                }

                "Pending" -> statusPaket.setTextColor(view.context.getColor(R.color.text_pending))
            }

            view.findViewById<TextView>(R.id.mulaiResult)
                .text = alat.tanggalMulai
            view.findViewById<TextView>(R.id.akhirResult)
                .text = alat.tanggalSelesai

            view.findViewById<Button>(R.id.checkBtn)
                .setOnClickListener {
                    mqtt.disconnect()
                    mqtt.connect { isConnected ->
                        checkServer(view, isConnected) {
                            if (isConnected) {
                                mqtt.subscribe(alat.idAlat + "/active") {
                                    checkAlat(it, view)
                                }
                                success.apply {
                                    title = getString(R.string.berhasil)
                                    animation = R.raw.lotisuccess
                                }.show()
                            } else {
                                alert.apply {
                                    title = getString(R.string.tidak_terhubung_server)
                                    message = getString(R.string.hubungin_admin_text)
                                    animation = R.raw.crosserror
                                    textBtn = getString(R.string.hubungin_admin)
                                }.show {
                                    utils.whatsapp("koneksi gagal" , "6281264628242")
                                }
                            }
                        }
                    }
                }

            viewMonitoring.jadwal.observe(viewLifecycleOwner) { jadwal ->
                if(jadwal != null) {
                    view.findViewById<Button>(R.id.tambahJadwal)
                        .visibility = View.GONE
                    view.findViewById<ConstraintLayout>(R.id.layoutJadwal)
                        .visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.namaPerangkat)
                        .text = utils.checkNameAlat(jadwal.namePaket, jadwal.idAlat)
                    view.findViewById<TextView>(R.id.hariResult)
                        .text = dayName(jadwal.days)
                    view.findViewById<TextView>(R.id.resultWaktuMulai)
                        .text = buildString {
                            append(jadwal.startTime)
                            append(" ")
                            append("WIB")
                        }
                    view.findViewById<TextView>(R.id.resultWaktuBerakhir)
                        .text = buildString {
                            append(jadwal.endTime)
                            append(" ")
                            append("WIB")
                        }
                    view.findViewById<Button>(R.id.switcher)
                        .setOnClickListener {
                            //todo: jadwal switch belum diisi fungsi
                        }
                    view.findViewById<Button>(R.id.hapus)
                        .setOnClickListener {
                            //todo: jadwal hapus belum diisi fungsi
                        }
                    view.findViewById<Button>(R.id.edit)
                        .setOnClickListener {
                            //todo: jadwal edit belum diisi fungsi
                        }
                } else {
                    val btnTambahJadwal = view.findViewById<Button>(R.id.tambahJadwal)
                    btnTambahJadwal.visibility = View.VISIBLE
                    btnTambahJadwal.setOnClickListener {
                        //todo: jadwal tambah belum diisi fungsi
                    }
                    view.findViewById<ConstraintLayout>(R.id.layoutJadwal)
                        .visibility = View.GONE

                }
            }

            view.findViewById<TextView>(R.id.infoTitleId)
                .text = getString(R.string.info)
            view.findViewById<TextView>(R.id.infoText)
                .text = getString(R.string.fitur_spy_text)
            val btnSpy = view.findViewById<Button>(R.id.btnSpy)
            val btnBunyikan = view.findViewById<Button>(R.id.bunyiBtn)
            btnBunyikan.setOnClickListener {
                mqtt.publish(alat.idAlat+ "/delay", "5")
                mqtt.publish(alat.idAlat+ "/alarm", "1")
                success.apply {
                    title = ""
                    animation = R.raw.lottie_count
                    delay = 5000
                    cancelable = false
                }.show()
            }
            viewMonitoring.mode.observe(viewLifecycleOwner) { mode ->
                when (mode) {
                    "otomatis" -> {
                        btnSpy.text = getString(R.string.mode_auto)
                        btnSpy.setOnClickListener {
                            btnSpy.text = getString(R.string.mode_manual)
                            viewMonitoring.mode.value = "manual"
                            mqtt.publish(alat.idAlat+ "/mode", "manual")
                            btnBunyikan.visibility = View.VISIBLE
                            btnSpy.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                bottomMargin = 0
                            }

                        }
                    }
                    "manual" -> {
                        btnSpy.text = getString(R.string.mode_manual)
                        btnBunyikan.visibility = View.VISIBLE
                        btnSpy.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            bottomMargin = 0
                        }
                        btnSpy.setOnClickListener {
                            btnSpy.text = getString(R.string.mode_auto)
                            viewMonitoring.mode.value = "otomatis"
                            mqtt.publish(alat.idAlat+ "/mode", "otomatis")
                            btnBunyikan.visibility = View.GONE
                            btnSpy.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                bottomMargin = 52
                            }
                        }
                    }
                }
            }

        }

    }

    private fun checkServer(view: View, boolean: Boolean, callback : (ok : Boolean) -> Unit = {}) {
        val koneksi = view.findViewById<TextView>(R.id.koneksi)
        val indicator = view.findViewById<ImageView>(R.id.indicator)
        view.post {
            if (boolean) {
                koneksi.text = getString(R.string.terhubung_server)
                koneksi.setTextColor(view.context.getColor(R.color.text_success))
                koneksi.contentDescription = getString(R.string.terhubung_server)
                indicator.backgroundTintList = view.context.getColorStateList(R.color.text_success)
                indicator.contentDescription = getString(R.string.terhubung_server)
                callback(true)
            } else {
                koneksi.text = getString(R.string.tidak_terhubung_server)
                koneksi.setTextColor(view.context.getColor(R.color.text_failed))
                koneksi.contentDescription = getString(R.string.tidak_terhubung_server)
                indicator.backgroundTintList = view.context.getColorStateList(R.color.text_failed)
                indicator.contentDescription = getString(R.string.tidak_terhubung_server)
                callback(false)
            }
        }
    }

    private fun checkAlat(result: String, view: View ) {
        val check = view.findViewById<TextView>(R.id.statusAlatResult)
        view.post {
            if (result == "1") {
                check.text = getString(R.string.aktif)
                check.contentDescription = getString(R.string.aktif)
                check.setTextColor(view.context.getColor(R.color.text_success))

            } else {
                check.text = getString(R.string.no_active)
                check.contentDescription = getString(R.string.no_active)
                check.setTextColor(view.context.getColor(R.color.text_failed))
            }
        }
    }
}