package com.example.tokoalarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivitySettings :AppCompatActivity() {

    private val nada  : List<Tone> = listOf(
        Tone("tone 1", R.raw.suara_satu),
        Tone("tone 2", R.raw.suara_dua),
        Tone("tone 3", R.raw.suara_tiga),
        Tone("tone 4", R.raw.suara_empat)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }
}