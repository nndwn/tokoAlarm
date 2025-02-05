package com.example.tokoalarm


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

//todo: alat dapat di input dengan nilai yang sama , seharus di berikan relasi ke user
class FragmentGetAlat :Fragment(R.layout.fragment_get_alat) {
    private lateinit var viewModel : SharedViewPilihPaket
    private lateinit var session: Session
    private lateinit var numbIdAlat: EditText
    private lateinit var loading: DialogLoading
    private lateinit var alert : DialogAlert
    private lateinit var success : DialogSuccess
    private var saldoInt :Int? = null
    private var harga : Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = Session(PrefManager(view.context))
        viewModel = ViewModelProvider(requireActivity())[SharedViewPilihPaket::class.java]
        numbIdAlat = view.findViewById(R.id.inputAlat)
        loading = DialogLoading(requireActivity())
        alert = DialogAlert(requireActivity())
        success = DialogSuccess(requireActivity())

        view.findViewById<TextView>(R.id.infoTitleId).text = getString(R.string.informasi)
        view.findViewById<TextView>(R.id.infoText).text = getString(R.string.get_alat)

        viewModel.saldo.observe(viewLifecycleOwner) {
            saldoInt = it
        }
        viewModel.paket.observe(viewLifecycleOwner) { data ->
            val position = viewModel.position.value!!
            view.findViewById<TextView>(R.id.namePaketResult).text = data[position].periode
            view.findViewById<TextView>(R.id.durasi_result).text =  buildString {
                append(data[position].dayConvertion)
                append(" ")
                append(getString(R.string.hari))
            }
            harga = data[position].biaya.toInt()
            println(harga)
            view.findViewById<TextView>(R.id.biaya_result).text = data[position].biayaRupiah
            view.findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.button)
                .setOnClickListener {
                    unFocus(view.context)
                    if (validation()) {
                        loading.startLoadingDialog()
                        viewModel.input(
                            session.getPhone()!!,
                            data[position],
                            session.getIdUser()!!,
                            numbIdAlat.text.toString().trim()
                        ) { status ->
                            when(status) {
                                "connection" -> {
                                    alert.show(
                                        getString(R.string.info),
                                        getString(R.string.trouble_connection),
                                        R.raw.crosserror
                                    )
                                }

                                "failed" -> {
                                    alert.show(
                                        getString(R.string.info),
                                        getString(R.string.id_salah),
                                        R.raw.crosserror
                                    )
                                }
                                "success" ->{
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


    private fun validation() : Boolean {
        return when {
            numbIdAlat.text.toString().isEmpty() -> {
                numbIdAlat.error = getString(R.string.empty_id_alat)
                numbIdAlat.requestFocus()
                false
            }
            saldoInt == null -> {
                alert.show(
                    getString(R.string.info),
                    getString(R.string.app_error),
                    R.raw.crosserror
                )
                false
            }
            saldoInt!! < harga -> {
                alert.show(
                    getString(R.string.info),
                    getString(R.string.saldo_tidak_mencukupin),
                    R.raw.crosserror
                ){
                    val intent = Intent(view?.context, ActivityTopUp::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                false
            }
            else -> true
        }
    }

    private fun unFocus(context: Context) {
        val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow( view?.windowToken, 0)
        numbIdAlat.clearFocus()
    }

}