package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class FragmentAccount : Fragment(R.layout.fragment_account) {

    private lateinit var prefManager: PrefManager
    private lateinit var utils: Utils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        prefManager = PrefManager(requireContext())
        utils = Utils(requireContext())

        val dev = view.findViewById<Button>(R.id.dev)
        btnLogout.setOnClickListener {
            logout()
            val intent = Intent(requireContext(), ActivityLogin::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        dev.text = "Tekan ini"
        dev.setOnClickListener {
            utils.whatsapp(prefManager.getTokenFcm.toString(), "628992046122")
        }
    }

    private fun logout() {
        prefManager.setPwd("")
        prefManager.setIdUser("")
        prefManager.setPhone("")
        prefManager.setNameUser("")
    }
}