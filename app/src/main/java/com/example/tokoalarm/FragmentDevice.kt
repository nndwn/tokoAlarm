package com.example.tokoalarm


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

//todo: pada nilai masa aktif terdapat bug nilai mines selalu bertambah kemungkinan jika user kembali pembayaran hanya mengurangsin nilai mines tersebut
//todo: pada api getalat terdapat Inp ut yang tidak diperlukan seperti "Status"

class FragmentDevice : Fragment(R.layout.layout_main_list), OnItemClickAdapterListDetail {

    private lateinit var viewModel: SharedViewMainActivity
    private lateinit var dialogInput: DialogInput
    private lateinit var listAlat: List<ListAlat>
    private lateinit var adapter: AdapterListDetail
    private lateinit var session: PrefManager
    private lateinit var alert: DialogAlert

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SharedViewMainActivity::class.java]
        session = PrefManager(requireContext())
        alert = DialogAlert(requireActivity())


        view.findViewById<TextView>(R.id.title_fragment).text = getString(R.string.daftar_detail_paket)

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
            text = if (nameAlat == "-" || nameAlat.isEmpty()) {
                buildString {
                    append(getString(R.string.alat))
                    append(" ")
                    append(listAlat[position].idAlat)
                }
            } else nameAlat
        }.show {
            if (dialogInput.text.length > 40 ){
                alert.show(
                    getString(R.string.perhatian),
                    getString(R.string.max_char_error),
                    R.raw.crosserror
                )
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
        println("test")
    }

    override fun onItemPerpanjang(position: Int) {
        val intent = Intent(requireContext(), ActivityBeliPaket::class.java)
        intent.putExtra("idAlat", listAlat[position].idAlat)
        viewModel.saldo.observe(viewLifecycleOwner){
            intent.putExtra("saldo", it)
        }
        startActivity(intent)
    }

    override fun onItemAdd(position: Int) {
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