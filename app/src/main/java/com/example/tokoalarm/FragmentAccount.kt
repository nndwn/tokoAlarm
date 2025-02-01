package com.example.tokoalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment

class FragmentAccount :Fragment(R.layout.fragment_account) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnNotificationOne = view.findViewById<Button>(R.id.testNotificationOne)
        val btnNotificationTwo = view.findViewById<Button>(R.id.testNotificationTwo)
        val btnNotificationThree = view.findViewById<Button>(R.id.testNotificationThree)

        btnLogout.setOnClickListener {
            val session = Session(PrefManager(requireContext()))
            session.logout()
            val intent = Intent(requireContext(), ActivityLogin::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

//        btnNotificationOne.setOnClickListener {
//            println("tombol satu")
//            Notif(requireContext(), "notifSatu", "notif satu", "test satu dua tiga", R.raw.suara_satu)
//        }
//
//        btnNotificationTwo.setOnClickListener {
//            println("tombol dua")
//            Notif(requireContext(), "notifDua", "notif dua", "test satu dua tiga samaple", R.raw.suara_dua)
//        }
//
//        btnNotificationThree.setOnClickListener {
//            println("tombol tiga")
//            Notif(requireContext(), "notifTiga", "notif tiga", "test satu dua tiga", R.raw.suara_tiga)
//        }
    }




}