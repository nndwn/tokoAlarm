package com.example.tokoalarm


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//todo: pada nilai masa aktif terdapat bug nilai mines selalu bertambah kemungkinan jika user kembali pembayaran hanya mengurangsin nilai mines tersebut
//todo: pada api getalat terdapat Inp ut yang tidak diperlukan seperti "Status"

class FragmentDevice : Fragment(R.layout.fragment_device), OnItemClickListener {

    private lateinit var viewModel: SharedViewMainActivity
    private lateinit var dialogInput: DialogInput
    private lateinit var listAlat: List<ListAlat>
    private lateinit var adapter: AdapterListDetail
    private lateinit var session: Session

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewMainActivity::class.java]
        session = Session(PrefManager(requireContext()))

        dialogInput = DialogInput(requireActivity()).apply {
            parent = view as ViewGroup
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.adapterList)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        viewModel.listAlat.observe(viewLifecycleOwner) {
            listAlat = sortListAlat(it)
            adapter = AdapterListDetail(listAlat, this)
            recyclerView.adapter = adapter
        }
    }

    private fun sortListAlat(listAlat : List<ListAlat>) : List<ListAlat> {
        return listAlat.sortedWith(compareByDescending<ListAlat> { it.status == "Aktif" }
            .thenByDescending { it.sisaHari.toIntOrNull() ?: 0 })
    }

    override fun onItemRename(position: Int) {
        val nameAlat = listAlat[position].namePaket
        dialogInput.apply {
            title = getString(R.string.ubah_name)
            text = nameAlat
        }.show {
            println(dialogInput.text)
            println(nameAlat)
            if (nameAlat != dialogInput.text ) {
                listAlat[position].namePaket = dialogInput.text
                viewModel.listAlat.value = listAlat
                adapter.notifyItemChanged(position)
                renameAlat(listAlat[position].idAlat, dialogInput.text)
            }
        }
    }

    override fun onItemMonitoring(position: Int) {
        TODO("Not yet implemented")
    }

    override fun onItemPerpanjang(position: Int) {
        TODO("Not yet implemented")
    }

    private fun renameAlat(idAlat: String, newName: String) {
        lifecycleScope.launch {
            val jsonBody = JsonObject().apply {
                addProperty("id_alat", idAlat)
                addProperty("no_hp", session.getPhone())
                addProperty("new_name", newName)
            }

            val call = RetrofitClient.apiService.renameAlat(API_KEY, jsonBody)

            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val status = it.get("status").asInt
                            val message = it.get("message").asString
                            if (status == 200) {
                                println("sucess")
                            } else {
                                println("not sucess")
                            }
                        }
                    } else {
                        println("fail")
                    }
                }

                override fun onFailure(p0: Call<JsonObject>, p1: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

}