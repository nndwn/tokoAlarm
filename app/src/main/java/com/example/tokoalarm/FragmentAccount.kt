package com.example.tokoalarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class FragmentAccount :Fragment(R.layout.fragment_account) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val session = Session(PrefManager(requireContext()))
            session.logout()
            val intent = Intent(requireContext(), ActivityLogin::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}