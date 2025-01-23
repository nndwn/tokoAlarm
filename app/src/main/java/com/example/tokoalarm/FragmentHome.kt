package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.launch

class FragmentHome :Fragment(R.layout.fragment_home), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var session : Session
    private lateinit var saldeText : TextView


    override fun onViewCreated(view : View, savedIntanceState: Bundle?) {
        super.onViewCreated(view, savedIntanceState)
        saldeText = view.findViewById(R.id.tvSaldo)
        session = Session(PreferencesManager(requireContext()))
        getIdUser()
        topUpBtn(view)
        historyBtn(view)

        swipeRefreshLayout = view.findViewById(R.id.containerSwipe)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.post {
            swipeRefreshLayout.isRefreshing = true
            getData()
        }


    }

    override fun onRefresh() {
        getData()
    }

    private fun topUpBtn(view: View) {
        val btn = view.findViewById<Button>(R.id.btnTopup)
        btn.setOnClickListener {
            val intent = Intent(activity, TopUp::class.java)
            startActivity(intent)
        }
    }

    private fun historyBtn(view: View) {
        val btn = view.findViewById<Button>(R.id.btnHistory)
        btn.setOnClickListener {
            val intent = Intent(activity, History::class.java)
            startActivity(intent)
        }
    }

    private fun getData() {
        if (isAdded) {
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.getDataPelanggan(
                        session.getIdUser()!!
                    )
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.status == true) {

                        }else {
                            throw Exception("problem in status response")
                        }
                    }else {
                        throw Exception("Response not successful")
                    }
                }catch (e : Exception){

                }
            }
        }
    }
    private fun getIdUser() {
        val errorDialog = ErrorDialog(requireActivity())
        if (session.getIdUser() == "")
            lifecycleScope.launch {
                try {
                    val response = RetrofitClient.apiService.login(
                        session.getPhone()!!,
                        session.getPwd()!!
                    )
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.status == true) {
                            val data = loginResponse.data
                            session.setIdUser(data.id)
                        } else {
                            throw Exception("problem in status response")
                        }
                    } else {
                        throw Exception("Response not successful")
                    }
                } catch (e: Exception) {
                    println("error $e")
                    errorDialog.startDialog(getString(R.string.info), getString(R.string.trouble_connection))
                }
            }
    }
}