package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentPilihPaket : Fragment(R.layout.layout_info_adapter) {

    private lateinit var viewModel: SharedViewPilihPaket
    private lateinit var listPaketView: RecyclerView
    private lateinit var alert: DialogAlert
    private lateinit var success: DialogSuccess
    private lateinit var loading: DialogLoading
    private lateinit var session: PrefManager
    private var idAlat: String? = null
    private var saldo: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewPilihPaket::class.java]
        alert = DialogAlert(requireActivity())
        success = DialogSuccess(requireActivity())
        loading = DialogLoading(requireActivity())
        session = PrefManager(view.context)

        view.findViewById<TextView>(R.id.infoTitleId)
            .text = getString(R.string.informasi)
        view.findViewById<TextView>(R.id.infoText)
            .text = getString(R.string.infoBeliPaket)

        viewModel.idAlat.observe(viewLifecycleOwner) {
            idAlat = it
        }
        viewModel.saldo.observe(viewLifecycleOwner) {
            saldo = it
        }

        listPaketView = view.findViewById(R.id.adapterList)
        listPaketView.layoutManager = LinearLayoutManager(view.context)
        viewModel.paket.observe(viewLifecycleOwner) { data ->
            listPaketView.adapter = AdapterListPaket(data) {
                val position = listPaketView.getChildAdapterPosition(it)
                viewModel.position.value = position
                if (idAlat.isNullOrEmpty()) {
                    val fragmentGetAlat = FragmentGetAlat()
                    parentFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                        )
                        .addToBackStack(null)
                        .replace(R.id.fragment, fragmentGetAlat)
                        .commit()

                } else if (saldo == null) {
                    alert.apply {
                        title = getString(R.string.perhatian)
                        message = getString(R.string.app_error)
                        animation = R.raw.crosserror
                    }.show()
                } else if (saldo!! < data[position].biaya.toInt()) {
                    alert.apply {
                        title = getString(R.string.perhatian)
                        message = getString(R.string.saldo_tidak_mencukupin)
                        animation = R.raw.crosserror
                    }
                        .show{
                            val intent = Intent(view.context, ActivityTopUp::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                } else {
                    loading.startLoadingDialog()
                    viewModel.input(
                        session.getPhone!!,
                        data[position],
                        session.getIdUser!!,
                        idAlat!!
                    ) { status ->
                        when (status) {
                            "connection" -> {
                                alert.apply {
                                    title = getString(R.string.perhatian)
                                    message = getString(R.string.trouble_connection)
                                    animation = R.raw.crosserror
                                }.show()

                            }

                            "failed" -> {
                                alert.apply {
                                    title = getString(R.string.perhatian)
                                    message = getString(R.string.trouble_connection)
                                    animation = R.raw.crosserror
                                }.show()
                            }

                            "success" -> {
                                success.apply {
                                    animation = R.raw.lotisuccess
                                    title = getString(R.string.berhasil)
                                }.show {
                                    val intent = Intent(view.context, ActivityMain::class.java)
                                    intent.putExtra("toFragment", getString(R.string.device))
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            }
                        }
                        loading.dismissDialog()
                    }
                }

            }
        }
    }
}