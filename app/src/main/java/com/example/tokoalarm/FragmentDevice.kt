package com.example.tokoalarm


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.util.ArrayList

//todo: pada nilai masa aktif terdapat bug nilai mines selalu bertambah kemungkinan jika user kembali pembayaran hanya mengurangsin nilai mines tersebut
//todo: pada api getalat terdapat Input yang tidak diperlukan seperti "Status"

class FragmentDevice : Fragment(R.layout.layout_main_list), OnItemClickAdapterListDetail {

    private lateinit var viewModel: SharedViewMainActivity
    private lateinit var dialogInput: DialogInput
    private var listAlat: List<ListAlat> = emptyList()
    private lateinit var adapter: AdapterListDetail
    private lateinit var session: PrefManager
    private lateinit var alert: DialogAlert
    private lateinit var utils: Utils
    private lateinit var recyclerView :RecyclerView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewMainActivity::class.java]
        session = PrefManager(requireContext())
        alert = DialogAlert(requireActivity())
        utils = Utils(requireContext())

        view.findViewById<TextView>(R.id.title_fragment).text = getString(R.string.daftar_detail_paket)

        dialogInput = DialogInput(requireActivity()).apply {
            parent = view as ViewGroup
        }

        recyclerView = view.findViewById(R.id.adapterList)

        recyclerView.layoutManager = LinearLayoutManager(view.context)
        viewModel.listAlat.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                listAlat = sortListAlat(it)
            }
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
            text = utils.checkNameAlat(nameAlat, listAlat[position].idAlat)

        }.show {
            if (dialogInput.text.length > 40 ){
                alert.apply {
                    title = getString(R.string.perhatian)
                    message = getString(R.string.max_char_error)
                    animation =  R.raw.crosserror
                }.show()
                return@show
            }
            if (nameAlat != dialogInput.text ) {
                listAlat[position].namePaket = dialogInput.text
                viewModel.listAlat.value = listAlat
                adapter.notifyItemChanged(position)
                renameAlat(listAlat[position].idAlat, dialogInput.text)
            }
        }
    }

    override fun onItemMonitoring(position: Int) {
        val intent = Intent(requireContext(), ActivityMonitoring::class.java)
        intent.putExtra("MonitorData", listAlat[position])
        viewModel.listJadwal.observe(viewLifecycleOwner) {
            intent.putParcelableArrayListExtra("JadwalData", ArrayList(it))
        }
        intent.putExtra("position", position)
        startActivity(intent)
    }

    override fun onItemPerpanjang(position: Int) {
        val intent = Intent(requireContext(), ActivityBeliPaket::class.java)
        intent.putExtra("idAlat", listAlat[position].idAlat)
        viewModel.saldo.observe(viewLifecycleOwner){
            intent.putExtra("saldo", it)
        }
        startActivity(intent)
    }

    override fun onItemAdd() {
        val intent = Intent(requireContext(), ActivityBeliPaket::class.java)
        viewModel.saldo.observe(viewLifecycleOwner){
            intent.putExtra("saldo", it)
        }
        startActivity(intent)
    }

    private fun renameAlat(idAlat: String, newName: String) {
        lifecycleScope.launch {
            val jsonBody = JsonObject().apply {
                addProperty("id_alat", idAlat)
                addProperty("no_hp", session.getPhone)
                addProperty("new_name", newName)
            }

            val response = RetrofitClient.apiService.renameAlat(API_KEY, jsonBody)
            if (response.isSuccessful) {
                val responseBody = response.body()
                responseBody?.let {
                    val status = it.get("status").asInt
                    val message = it.get("message").asString
                    if (status == 200) {
                        println("success")
                    } else {
                        println("not success: $message")
                    }
                }
            } else {
                println("fail: ${response.code()} - ${response.message()}")
            }
        }

    }

}