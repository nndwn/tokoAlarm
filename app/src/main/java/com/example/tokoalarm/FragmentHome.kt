package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch


class FragmentHome :Fragment(R.layout.fragment_home), SwipeRefreshLayout.OnRefreshListener{

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var session : Session
    private lateinit var saldoText : TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var adapterPromoList : AdapterListPromo
    private lateinit var prefManager: PrefManager
    private lateinit var alert : DialogAlert

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            if (viewPager.currentItem == adapterPromoList.itemCount - 1) {
                viewPager.currentItem = 0
            } else {
                viewPager.currentItem++
            }
            handler.postDelayed(this, 3000)
        }
    }

    private var linkPemesanan :String? = null


    override fun onViewCreated(view : View, savedIntanceState: Bundle?) {
        super.onViewCreated(view, savedIntanceState)
        prefManager = PrefManager(requireContext())

        saldoText = view.findViewById(R.id.tvSaldo)
        session = Session(prefManager)
        alert = DialogAlert(requireActivity())


        topUpBtn(view)
        historyBtn(view)
        tutorialBtn(view)
        settingBtn(view)
        beliPaketBtn(view)
        purposeBtn(view)

        swipeRefreshLayout = view.findViewById(R.id.containerSwipe)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.post {
            if (isAdded) {
                swipeRefreshLayout.isRefreshing = true
                getData()
            }
        }

        viewPager = view.findViewById(R.id.viewPager)

        lifecycleScope.launch {
            prefManager.imagePathsFlow.collect { imagePaths ->
                adapterPromoList= AdapterListPromo(imagePaths.toList()) {
                    val position = viewPager.currentItem
                    linkBanner(position)
                }
                viewPager.adapter = adapterPromoList
            }
        }
        handler.postDelayed(runnable, 3000)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }

    override fun onRefresh() {
       getData()
    }

    private fun linkBanner(i :Int) {
        val url = DataManual().banner[i]
        val intent = Intent(context, ActivityWebView::class.java);
        intent.putExtra("URL", url);
        startActivity(intent);
    }

    private fun tutorialBtn(view: View) {
        val btn = view.findViewById<LinearLayout>(R.id.lnCaraPenggunaan)
        btn.setOnClickListener{
            val intent = Intent(activity, ActivityWebView::class.java)
            intent.putExtra("URL","https://tokoalarm.com/cara-penggunaan/" )
            startActivity(intent)
        }
    }

    private fun purposeBtn(view: View) {
        val btn = view.findViewById<LinearLayout>(R.id.lnPesanAlarm)
        btn.setOnClickListener {
            val intent = Intent(activity, ActivityWebView::class.java)
            if (linkPemesanan ==null) {
                getData()
            }
            intent.putExtra("URL", linkPemesanan)
            startActivity(intent)
        }
    }

    private fun beliPaketBtn(view: View) {
        val btn = view.findViewById<LinearLayout>(R.id.lnBeliPaket)
        btn.setOnClickListener {
            val intent = Intent(activity, ActivityBeliPaket::class.java)
            startActivity(intent)
        }
    }

    private fun settingBtn(view: View) {
        val btn = view.findViewById<LinearLayout>(R.id.lnSetting)
        btn.setOnClickListener {
            val intent = Intent(activity, ActivitySettings::class.java)
            startActivity(intent)
        }
    }

    private fun topUpBtn(view: View) {
        val btn = view.findViewById<Button>(R.id.btnTopup)
        btn.setOnClickListener {
            val intent = Intent(activity, ActivityTopUp::class.java)
            startActivity(intent)
        }
    }

    private fun historyBtn(view: View) {
        val btn = view.findViewById<Button>(R.id.btnHistory)
        btn.setOnClickListener {
            val intent = Intent(activity, ActivityHistory::class.java)
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
                        val responseData = response.body()
                        if (responseData?.status == true) {
                            saldoText.text = responseData.saldo
                            linkPemesanan = responseData.config.linkPesanAlarm

                            swipeRefreshLayout.isRefreshing = false
                        }else {
                            throw Exception("problem in status response")
                        }
                    }else {
                        throw Exception("Response not successful")
                    }
                }catch (e : Exception){
                    swipeRefreshLayout.isRefreshing = false
                    println("getData $e")
                    alert.show(getString(R.string.info),
                        getString(R.string.trouble_connection),
                        R.raw.crosserror
                    )
                }
            }
        }
    }
}