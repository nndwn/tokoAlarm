package com.example.tokoalarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentTopUp :Fragment(R.layout.fragment_top_up) {

    private lateinit var listPriceView :RecyclerView
    private lateinit var viewModel: SharedViewTopUp

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = DataManual()

        viewModel = ViewModelProvider(requireActivity())[SharedViewTopUp::class.java]

        val titleInfo = view.findViewById<TextView>(R.id.infoTitleId)
        titleInfo.text = getString(R.string.informasi)
        val textInfo = view.findViewById<TextView>(R.id.infoText)
        textInfo.text = getString(R.string.pilih_harga)

        listPriceView = view.findViewById(R.id.adapterList)
        listPriceView.layoutManager = LinearLayoutManager(view.context)
        listPriceView.adapter = AdapterListTopUp(data.listPrice) {
            val position = listPriceView.getChildAdapterPosition(it)
            viewModel.price.value = data.listPrice[position].price
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right)
                .addToBackStack(null)
                .replace(R.id.fragment_topUp, FragmentTopUpBank())
                .commit()

        }
    }
}