package com.example.tokoalarm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial

class FragmentMonitoring : Fragment(R.layout.fragment_monitoring), OnListenerSensor {

    private lateinit var utils: Utils
    private lateinit var alert: DialogAlert
    private lateinit var success: DialogSuccess
    private lateinit var input: DialogInput
    private lateinit var prefManager: PrefManager


    private val viewModelMonitor: ViewModelMonitor by activityViewModels()
    private lateinit var sensorContainer: RecyclerView

    private var activeAlat = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utils = Utils(requireContext())
        success = DialogSuccess(requireActivity())
        input = DialogInput(requireActivity())
        prefManager = PrefManager(requireContext())


        viewModelMonitor.alat.observe(viewLifecycleOwner) { alat ->
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
                    checkServer(false, alat.idAlat)
                }

                "Aktif" -> {
                    statusPaket.setTextColor(view.context.getColor(R.color.text_success))
                    containerStatusAlat.visibility = View.VISIBLE
                    viewModelMonitor.connectionStatus.observe(viewLifecycleOwner) { isConnected ->
                        checkServer(isConnected, alat.idAlat)
                    }
                    sensorContainer = view.findViewById(R.id.listSensor)
                    sensorContainer.layoutManager = LinearLayoutManager(view.context)
                    viewModelMonitor.sensor.observe(viewLifecycleOwner) {
                        sensorContainer.adapter = AdapterListSensor(it, this)

                    }
                }
            }

            view.findViewById<TextView>(R.id.mulaiResult)
                .text = alat.tanggalMulai
            view.findViewById<TextView>(R.id.akhirResult)
                .text = alat.tanggalSelesai

            viewModelMonitor.jadwal.observe(viewLifecycleOwner) { jadwal ->
                if (jadwal != null) {
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
                    view.findViewById<SwitchCompat>(R.id.switcher)
                        .setOnCheckedChangeListener { _, isChecked ->

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
            val btnLamaBunyi = view.findViewById<Button>(R.id.lamaBunyi)


            btnLamaBunyi.setOnClickListener {
                input.apply {
                    title = buildString {
                        append(getString(R.string.atur_lama_bunyi))
                        append(" ")
                        append(getString(R.string.satuan_detik))
                    }
                    text =
                        if (viewModelMonitor.delay.value != 0L) "5" else viewModelMonitor.delay.value.toString()
                }.show {
                    viewModelMonitor.publish(alat.idAlat + "/delay", input.text)
                }
            }
            btnBunyikan.setOnClickListener {
                viewModelMonitor.publish(alat.idAlat + "/alarm", "1")
                btnBunyikan.isEnabled = false
                Handler(Looper.getMainLooper()).postDelayed({
                    btnBunyikan.isEnabled = true
                }, viewModelMonitor.delay.value?.times(1000) ?: 5000)
            }
            viewModelMonitor.mode.observe(viewLifecycleOwner) { mode ->
                when (mode) {
                    "otomatis" -> {
                        btnSpy.text = getString(R.string.mode_auto)
                        btnSpy.setOnClickListener {
                            btnSpy.text = getString(R.string.mode_manual)
                            viewModelMonitor.mode.value = "manual"
                            viewModelMonitor.publish(alat.idAlat + "/mode", "manual")
                            btnBunyikan.visibility = View.VISIBLE
                            btnLamaBunyi.visibility = View.VISIBLE

                            val fadeIn =
                                AnimationUtils.loadAnimation(view.context, android.R.anim.fade_in)
                            btnBunyikan.startAnimation(fadeIn)
                            btnLamaBunyi.startAnimation(fadeIn)
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
                            viewModelMonitor.mode.value = "otomatis"
                            viewModelMonitor.publish(alat.idAlat + "/mode", "otomatis")
                            btnBunyikan.visibility = View.GONE
                            btnLamaBunyi.visibility = View.GONE
                            btnSpy.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                bottomMargin = 52
                            }
                            val fadeOut =
                                AnimationUtils.loadAnimation(view.context, android.R.anim.fade_out)
                            btnLamaBunyi.startAnimation(fadeOut)
                            btnBunyikan.startAnimation(fadeOut)
                        }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModelMonitor.connect()
    }


    private fun checkServer(con: Boolean, idAlat: String) {
        val views = requireView()
        val koneksi = views.findViewById<TextView>(R.id.koneksi)
        val indicator = views.findViewById<ImageView>(R.id.indicator)
        if (con) {
            koneksi.text = getString(R.string.terhubung_server)
            koneksi.setTextColor(views.context.getColor(R.color.text_success))
            koneksi.contentDescription = getString(R.string.terhubung_server)
            indicator.backgroundTintList = views.context.getColorStateList(R.color.text_success)
            indicator.contentDescription = getString(R.string.terhubung_server)
            viewModelMonitor.subsAlat(buildString {
                append(idAlat)
                append("/active")
            }, "alat")
            viewModelMonitor.alatConnectionStatus.observe(viewLifecycleOwner) {
                checkAlat(it)
            }
        } else {
            koneksi.text = getString(R.string.tidak_terhubung_server)
            koneksi.setTextColor(views.context.getColor(R.color.text_failed))
            koneksi.contentDescription = getString(R.string.tidak_terhubung_server)
            indicator.backgroundTintList = views.context.getColorStateList(R.color.text_failed)
            indicator.contentDescription = getString(R.string.tidak_terhubung_server)
            activeAlat = false
        }
    }

    private fun checkAlat(result: Boolean) {
        val views = requireView()
        val check = views.findViewById<TextView>(R.id.statusAlatResult)
        if (result) {
            check.text = getString(R.string.aktif)
            check.contentDescription = getString(R.string.aktif)
            check.setTextColor(views.context.getColor(R.color.text_success))
            activeAlat = true
        } else {
            check.text = getString(R.string.no_active)
            check.contentDescription = getString(R.string.no_active)
            check.setTextColor(views.context.getColor(R.color.text_failed))
            activeAlat = false
        }
    }

    override fun rename(position: Int) {
        val data = viewModelMonitor.sensor.value!!
        input.apply {
            title = getString(R.string.rename_sensor)
            text = data[position].rename.ifEmpty {
                data[position].name
            }
        } .show {
            if (input.text.length > 30) {
                input.text = input.text.substring(0, 30)
            }
            viewModelMonitor.sensor.value!![position].rename = input.text
            sensorContainer.adapter?.notifyItemChanged(position)
            viewModelMonitor.renameSensor(
                prefManager.getPhone!!,
                viewModelMonitor.alat.value!!.idAlat,
                viewModelMonitor.sensor.value!![position].type,
                viewModelMonitor.sensor.value!![position].rename
            )
        }
    }

    override fun switcher(position: Int, isChecked: Boolean) {
       if (activeAlat){
           if (isChecked) {
               viewModelMonitor.publish( buildString {
                   append(viewModelMonitor.alat.value!!.idAlat)
                   append(viewModelMonitor.sensor.value!![position].typeStatin)
               }, "1") {
                   viewModelMonitor.subsAlat(buildString {
                       append(viewModelMonitor.alat.value!!.idAlat)
                       append(viewModelMonitor.sensor.value!![position].typeStatin)
                   },viewModelMonitor.sensor.value!![position].type)
               }
           } else {
               viewModelMonitor.unSubsAlat(buildString {
                   append(viewModelMonitor.alat.value!!.idAlat)
                   append(viewModelMonitor.sensor.value!![position].typeStatin)
               })
               viewModelMonitor.publish( buildString {
                   append(viewModelMonitor.alat.value!!.idAlat)
                   append(viewModelMonitor.sensor.value!![position].typeStatin)
               }, "0")
           }
       }

    }

    override fun check(check: Boolean) {
        println(check)
    }



}