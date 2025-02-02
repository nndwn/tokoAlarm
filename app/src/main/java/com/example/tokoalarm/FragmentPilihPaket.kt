package com.example.tokoalarm

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentPilihPaket :Fragment(R.layout.fragment_top_up) {

    private lateinit var viewModel : SharedViewPilihPaket
    private lateinit var listPaketView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewPilihPaket::class.java]

        view.findViewById<TextView>(R.id.infoTitleId)
            .text = getString(R.string.informasi)
        view.findViewById<TextView>(R.id.infoText)
            .text = getString(R.string.infoBeliPaket)

        listPaketView = view.findViewById(R.id.adapterList)
        listPaketView.layoutManager = LinearLayoutManager(view.context)
        viewModel.paket.observe(viewLifecycleOwner) { data ->
            listPaketView.adapter = AdapterListPaket(data) {
               val position = listPaketView.getChildAdapterPosition(it)
                viewModel.position.value = position

                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right)
                    .addToBackStack(null)
                    .replace(R.id.fragment, FragmentGetAlat())
                    .commit()
            }
        }
    }
}