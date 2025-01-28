package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlin.random.Random


class FragmentTopUpConfirm :Fragment(R.layout.fragment_top_up_confirm){

    private lateinit var viewModel: SharedViewTopUp
    private var selectedPayment :String? = null
    private var price : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewTopUp::class.java]
        val session = Session(PrefManager(view.context))
        val utils = Utils(requireContext())

        view.findViewById<TextView>(R.id.infoTitleId)
            .text = getString(R.string.informasi)
        view.findViewById<TextView>(R.id.infoText)
            .text = getString(R.string.cara_pembayaran)

        viewModel.price.observe(viewLifecycleOwner) {
            price = formatRupiah(it + Random.nextInt(500))
            view.findViewById<TextView>(R.id.top_up_price)
                .text = price
        }
        viewModel.methodPayment.observe(viewLifecycleOwner) {
            selectedPayment = it.name
            view.findViewById<TextView>(R.id.pembayaran_top_up)
                .text = selectedPayment
            view.findViewById<TextView>(R.id.rekening_top_up)
                .text = it.numberRek
            view.findViewById<TextView>(R.id.nama_rek_top_up)
                .text = it.owner


        }


        view.findViewById<Button>(R.id.konfirmasi_btn_topup)
            .setOnClickListener {
                val message = "Top Up: \n username: ${session.getNameUser()} \n metode : ${selectedPayment}\nPembayaran:${price}"
                utils.whatsapp(message)

            }
        view.findViewById<Button>(R.id.batal_btn_topup)
            .setOnClickListener {
                backToHome()
            }
    }

    private fun backToHome() {
        val intent = Intent(activity, ActivityMain::class.java)
        startActivity(intent)
    }
}