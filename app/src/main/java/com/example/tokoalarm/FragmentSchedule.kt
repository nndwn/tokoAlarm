package com.example.tokoalarm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentSchedule : Fragment(R.layout.fragment_schedule), OnItemClickAdapterListJadwal {

    private lateinit var viewModel: SharedViewMainActivity
    private lateinit var adapter: AdapterListJadwal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewMainActivity::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.adapterList)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        viewModel.listJadwal.observe(viewLifecycleOwner) {
            adapter = AdapterListJadwal(it, this)
            recyclerView.adapter = adapter
        }
    }

    override fun onItemEdit(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemDelete(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemSwitch(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemAdd(position: Int) {
        TODO("Not yet implemented")
    }


}